from fastapi import FastAPI
from routers import restaurants, reviews, qa

app = FastAPI(title="🍜 식당 추천 & QA API")

# 각 라우터 등록
app.include_router(restaurants.router, prefix="/restaurants")
app.include_router(reviews.router, prefix="/reviews")
app.include_router(qa.router, prefix="/qa")

# 서버 시작 시 LangChain QA 시스템 초기화
@app.on_event("startup")
async def startup_event():
    from services.qa_service import build_knowledge_base
    build_knowledge_base()