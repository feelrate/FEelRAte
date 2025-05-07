import requests

SPRING_SERVER = "http://localhost:8080"

# 🔐 JWT 토큰을 여기 변수에 저장해둬 (예: 로그인 후 발급받은 토큰)
JWT_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3NDYyNzc3NzksImV4cCI6MTc0NjM2NDE3OSwic3ViIjoia2ltY2hhbmhvMTExQGdtYWlsLmNvbSIsImlkIjoxfQ.smMztWKSZyBZFZnHBHcZ38Poxn4vRCSKuO7v3uwOTFE"

def send_restaurant(restaurant: dict) -> tuple:
    url = f"{SPRING_SERVER}/api/restaurants"
    headers = {"Authorization": f"Bearer {JWT_TOKEN}"}
    response = requests.post(url, json=restaurant, headers=headers)
    return response.status_code, response.text

def send_reviews(place_id: str, reviews: list) -> tuple:
    url = f"{SPRING_SERVER}/api/restaurants/{place_id}/reviews"
    headers = {"Authorization": f"Bearer {JWT_TOKEN}"}
    response = requests.post(url, json=reviews, headers=headers)
    return response.status_code, response.text
