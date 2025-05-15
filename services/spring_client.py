import requests

# Spring 서버 주소 및 JWT 인증
SPRING_SERVER = "http://localhost:8080"
JWT_TOKEN = "ey..."  # 실제 토큰 입력

headers = {"Authorization": f"Bearer {JWT_TOKEN}"}

# ✅ 식당 목록 가져오기
def fetch_restaurants():
    url = f"{SPRING_SERVER}/api/restaurants"
    return requests.get(url, headers=headers).json()

# ✅ 특정 식당의 리뷰 가져오기
def fetch_reviews(place_id):
    url = f"{SPRING_SERVER}/api/restaurants/{place_id}/reviews"
    return requests.get(url, headers=headers).json()