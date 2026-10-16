# Campus Hub Backend

## Cấu trúc dự án

```
Test45/
├── src/
│   ├── main/
│   │   ├── java/com/example/campushub/
│   │   │   ├── CampusHubApplication.java
│   │   │   ├── announcement/
│   │   │   │   ├── Announcement.java            ← JPA Entity
│   │   │   │   ├── AnnouncementRepository.java  ← Spring Data JPA
│   │   │   │   ├── AnnouncementService.java     ← Business logic
│   │   │   │   ├── AnnouncementController.java  ← REST endpoints
│   │   │   │   └── AnnouncementNotFoundException.java
│   │   │   └── common/
│   │   │       └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/com/example/campushub/
│       │   ├── CampusHubApplicationTests.java
│       │   └── announcement/
│       │       └── AnnouncementControllerTest.java
│       └── resources/
│           └── application.properties           ← H2 in-memory cho test
├── Dockerfile
├── .dockerignore
├── compose.yml
├── build.gradle
├── settings.gradle
└── README.md
```

---

## Phần 1 — Chạy kiểm thử và build Docker Image

### 1. Chạy Gradle test

```bash
./gradlew test
```

> Kết quả: tất cả test pass, sử dụng H2 in-memory (không cần MySQL).

### 2. Build Docker Image

```bash
docker build -t campus-hub-backend .
```

### 3. Xem danh sách image

```bash
docker images
```

---

## Phần 2 — Chạy hệ thống bằng Docker Compose

### Khởi động tất cả service

```bash
docker compose up -d
```

### Kiểm tra trạng thái service

```bash
docker compose ps
```

### Kiểm tra health endpoint

```bash
curl http://localhost:8080/actuator/health
```

Kết quả mong đợi:

```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "diskSpace": { "status": "UP" },
    "ping": { "status": "UP" }
  }
}
```

### Dừng hệ thống

```bash
docker compose down
```

### Dừng và xóa volume (reset dữ liệu)

```bash
docker compose down -v
```

---

## Cổng và URL

| Service | Cổng | URL |
|---------|------|-----|
| Backend | 8080 | http://localhost:8080 |
| Health  | 8080 | http://localhost:8080/actuator/health |
| MySQL   | 3306 | localhost:3306 (internal: `mysql:3306`) |

### API Endpoints

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET    | `/api/announcements` | Lấy tất cả thông báo |
| GET    | `/api/announcements/{id}` | Lấy thông báo theo ID |
| GET    | `/api/announcements/category/{category}` | Lọc theo danh mục |
| POST   | `/api/announcements` | Tạo thông báo mới |
| PUT    | `/api/announcements/{id}` | Cập nhật thông báo |
| DELETE | `/api/announcements/{id}` | Xóa thông báo |

---

## Biến môi trường

Backend nhận cấu hình kết nối MySQL qua biến môi trường (được định nghĩa trong `compose.yml`):

| Biến | Giá trị trong Compose | Mô tả |
|------|-----------------------|-------|
| `DB_HOST` | `mysql` | Tên service MySQL (thay localhost) |
| `DB_PORT` | `3306` | Cổng MySQL |
| `DB_NAME` | `campushub` | Tên database |
| `DB_USER` | `campushub` | Username |
| `DB_PASSWORD` | `campushub123` | Password |
| `SERVER_PORT` | `8080` | Cổng backend |

---

