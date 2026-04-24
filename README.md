# 🚀 Spring Boot Social Gateway (Posts + Comments + Virality Engine)

A backend system built with **Spring Boot**, **PostgreSQL**, and **Redis** that supports:

* Creating posts
* Adding comments (with depth control)
* Bot vs Human interaction handling
* Virality scoring using Redis
* Rate limiting & cooldown for bots
* Simple frontend integration

---

## 📌 Features

### 📝 Post Management

* Create a post
* Get all posts
* Get post by ID

### 💬 Comments System

* Add comments to posts
* Nested comments (depth limit = 20)
* Fetch comments by post

### 🤖 Bot Control System

* Max **100 bot replies per post**
* Cooldown enforcement between bot interactions

### 🔥 Virality Engine (Redis)

* Tracks post engagement
* Updates score based on:

  * Human comments
  * Bot replies

---

## 🛠️ Tech Stack

* **Backend:** Spring Boot (Java)
* **Database:** PostgreSQL
* **Cache / Logic Engine:** Redis
* **Build Tool:** Maven
* **Frontend:** Basic HTML + JS (optional)
* **Containerization:** Docker

---

## 📁 Project Structure

```
src/
 └── main/
     └── java/com/nikhitha/gateway/
         ├── controller/
         ├── entity/
         ├── repository/
         ├── service/
         └── config/
```

---

## ⚙️ Setup Instructions

### 1️⃣ Clone Repository

```
git clone https://github.com/your-username/your-repo.git
cd gateway
```

---

### 2️⃣ Start Dependencies (Docker)

Make sure Docker is running, then:

```
docker run -d -p 5432:5432 --name pg_container \
-e POSTGRES_DB=testdb \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASSWORD=postgres \
postgres:15

docker run -d -p 6379:6379 --name redis_container redis:7
```

---

### 3️⃣ Configure Application

Edit `application.properties`:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/testdb
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=update

spring.redis.host=localhost
spring.redis.port=6379
```

---

### 4️⃣ Run Application

```
mvn clean spring-boot:run
```

Server runs on:

```
http://localhost:8080
```

---

## 📡 API Endpoints

### 🔹 Create Post

```
POST /api/posts
```

Body:

```
{
  "authorId": 1,
  "authorType": "USER",
  "content": "Hello world"
}
```

---

### 🔹 Get All Posts

```
GET /api/posts
```

---

### 🔹 Add Comment

```
POST /api/posts/{postId}/comments
```

Body:

```
{
  "authorId": 2,
  "authorType": "USER",
  "content": "Nice post!",
  "depthLevel": 1
}
```

---

### 🔹 Get Comments

```
GET /api/posts/{postId}/comments
```

---

## 🔥 Redis Keys Used

| Key                                   | Description           |
| ------------------------------------- | --------------------- |
| `post:{id}:virality_score`            | Tracks engagement     |
| `post:{id}:bot_count`                 | Number of bot replies |
| `cooldown:bot_{botId}:human_{userId}` | Cooldown control      |

---

## 🧪 Testing

### Using Postman or Frontend:

* Create post
* Add comments (USER / BOT)
* Check Redis:

```
docker exec -it redis_container redis-cli
GET post:1:virality_score
GET post:1:bot_count
```

---

## ⚠️ Constraints Implemented

* Max comment depth = **20**
* Max bot replies per post = **100**
* Bot cooldown enforcement

---

## 🌐 Frontend (Optional)

Basic UI included to:

* Create posts
* Add comments
* View posts/comments

Run using:

```
Live Server (VS Code)
```

---

## 🚀 Deployment

Can be deployed using:

* Railway
* Render
* Docker

---

## 📌 Future Improvements

* Authentication (JWT)
* Post owner tracking
* Pagination & sorting
* Trending posts API
* UI improvements

---

## 👨‍💻 Author

**Pavan Kumar**

---

## ⭐ If you like this project

Give it a star on GitHub ⭐
