from langchain_core.tools import tool
from langchain_core.prompts import PromptTemplate
from langchain_ollama import OllamaLLM as Ollama
from dotenv import load_dotenv
import requests
import os

# 로컬 Ollama 모델 사용
llm = Ollama(model="llama3")
#env파일 로딩
load_dotenv()
# 프롬프트: 장소명만 추출하도록 요청
GOOGLE_MAPS_API_KEY = os.getenv("GOOGLE_MAPS_API_KEY")
prompt = PromptTemplate.from_template(
    "다음 문장에서 장소명(예: 지역, 건물, 역 등)만 정확히 추출해줘. 설명하지 말고 장소명만 말해 .문장: {text}"
)

# -------------------- 장소 추출 --------------------
def get_location_from_text(text: str) -> str:
    """
    사용자의 자연어 입력에서 장소명만 추출해 반환합니다. (예: 성신여대역, 홍대입구 등)
    """
    chain = prompt | llm
    result = chain.invoke({"text": text})
    return result.strip()
# -------------------- 위도/경도 변환 --------------------
def get_coordinates_from_location(location: str) -> str: 
    """
    장소명을 위도, 경도로 변환합니다. (예: 성신여자대학교 → 37.5928015,127.0166047)
    Google Maps Geocoding API 사용
    """
    url = "https://maps.googleapis.com/maps/api/geocode/json"
    params = {
        "address": location,
        "key": GOOGLE_MAPS_API_KEY,
    }
   
    try:
        response = requests.get(url, params=params)
        data = response.json()

        if data.get("status") == "OK" and data.get("results"):
            location_data = data["results"][0].get("geometry", {}).get("location", {})
            lat = location_data.get("lat")
            lng = location_data.get("lng")
            if lat is not None and lng is not None:
                return {
                    "latitude": lat, "longitude": lng
                }
            else:
                return {"error": "위도 또는 경도 정보가 없습니다."}
        else:
            return {
                "error": f"API 실패: {data.get('status')}, {data.get('error_message', '원인 미상')}"
            }
    except Exception as e:
        return {"error": f"예외 발생: {str(e)}"}
    
    # -------------------- 음식점 검색 --------------------
def get_nearby_restaurants(latitude: float, longitude: float, radius: int = 400) -> dict:
    """
    위도, 경도를 기반으로 주변의 식당을 검색합니다.
    Google Maps Places Nearby Search API 사용
    """
    url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
    params = {
        "location": f"{latitude},{longitude}",
        "radius": radius,
        "type": "restaurant",
        "language": "ko",
        "key": GOOGLE_MAPS_API_KEY
    }

    try:
        response = requests.get(url, params=params)
        data = response.json()

        if data.get("status") == "OK":
            restaurants = []
            for place in data.get("results", []):
                rating = place.get("rating", 0)
                review_count = place.get("user_ratings_total", 0)

                if rating is None or review_count is None:
                    continue  # 값이 없는 경우 제외

                if rating >= 3.0 and review_count >= 5:
                    restaurants.append({
                        "place_id": place.get("place_id"),
                        "name": place.get("name"),
                        "address": place.get("vicinity"),
                        "rating": rating,
                        "user_ratings_total": review_count,
                        "location": place.get("geometry", {}).get("location", {})
                    })
            return {"restaurants": restaurants}
        else:
            return {
                "error": f"API 실패: {data.get('status')}, {data.get('error_message', '원인 미상')}"
            }
    except Exception as e:
        return {"error": f"예외 발생: {str(e)}"}


   # -------------------- 리뷰 검색 -------------------- 
def get_place_details(place_id: str) -> dict:
    url = "https://maps.googleapis.com/maps/api/place/details/json"
    params = {
        "place_id": place_id,
        "key": GOOGLE_MAPS_API_KEY,
        "language": "ko",
        "fields": "name,review,rating,opening_hours"
    }

    try:
        response = requests.get(url, params=params)
        data = response.json()

        if data.get("status") != "OK":
            return {
                "error": f"API 오류: {data.get('status')}, {data.get('error_message', '원인 미상')}"
            }

        result = data.get("result", {})

        return {
            "name": result.get("name"),
            "opening_hours": result.get("opening_hours", {}).get("weekday_text"),
            "rating": result.get("rating"),
            "reviews": [
                {
                    "author": review.get("author_name"),
                    "rating": review.get("rating"),
                    "text": review.get("text")
                }
                for review in result.get("reviews", [])
            ]
        }

    except Exception as e:
        return {"error": f"예외 발생: {str(e)}"}
    


@tool
def recommend_restaurants_from_text(text: str) -> dict:
    """
    자연어 입력에서 장소를 추출하고, 그 주변 음식점을 추천합니다.
    (예: "성신여대 근처에서 혼밥하기 좋은 곳 추천해줘")
    """
    location = get_location_from_text(text)
    coords = get_coordinates_from_location(location)
    if "error" in coords:
        return coords
    restaurant=get_nearby_restaurants(coords["latitude"], coords["longitude"])
    if "error" in restaurant:
        return restaurant
    details = get_place_details(restaurant["restaurants"][0]["place_id"])
    if "error" in details:
        return details
    return details