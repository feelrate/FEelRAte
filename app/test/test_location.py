import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from service.restaurant_pipeline import process_and_store_restaurants

if __name__ == "__main__":
    input_text = "수원역1호선 근처  혼밥하기 좋은 곳 추천해줘"
    
    process_and_store_restaurants(input_text)


