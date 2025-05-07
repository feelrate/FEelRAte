from tools.location_tools import (
    get_location_from_text,
    get_coordinates_from_location,
    get_nearby_restaurants,
    get_place_details,
)
def process_and_store_restaurants(text: str):
    """
    자연어 입력 → 장소 추출 → 위도/경도 변환 → 식당 검색 → 상세정보(영업시간, 리뷰) → Spring 서버 저장
    """
    location = get_location_from_text(text)
    print(f"📍 추출된 장소: {location}")

    coords = get_coordinates_from_location(location)
    if "error" in coords:
        return coords

    restaurants_data = get_nearby_restaurants(coords["latitude"], coords["longitude"])
    if "error" in restaurants_data:
        return restaurants_data

    for place in restaurants_data["restaurants"]:
        details = get_place_details(place["place_id"])
        if "error" in details:
            print(f"❌ 상세정보 조회 실패: {place['name']} - {details['error']}")
            continue

        # 병합
        restaurant_payload = {
            "placeId": place["place_id"],
            "name": place["name"],
            "address": place["address"],
            "rating": place["rating"],
            "latitude": place["location"]["lat"],
            "longitude": place["location"]["lng"],
            "openingHours": details.get("opening_hours")  # details에서 가져옴
        }

        # 서버에 저장
        status_r, msg_r = send_restaurant(restaurant_payload)
        print(f"✅ 식당 저장: {place['name']} ({status_r}) - {msg_r}")

        # 리뷰 저장
        status_v, msg_v = send_reviews(place["place_id"], details.get("reviews", []))
        print(f"📝 리뷰 저장: {len(details.get('reviews', []))}개 ({status_v}) - {msg_v}")


from send_to_server import send_restaurant, send_reviews
