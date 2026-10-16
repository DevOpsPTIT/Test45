# Campus Hub Backend

## Thông tin sinh viên

| Họ tên | Mã sinh viên |
|--------|-------------|
| *(Điền họ tên)* | *(Điền MSSV)* |

---

## Cấu trúc dự án

```
Test45/
├── src/
│   ├── main/
│   │   ├── java/vn/edu/ptit/campushub/
│   │   │   ├── CampusHubApplication.java
│   │   │   ├── auth/          ← Xác thực
│   │   │   ├── category/      ← Danh mục bài đăng
│   │   │   ├── comment/       ← Bình luận
│   │   │   ├── common/        ← Xử lý lỗi chung
│   │   │   ├── config/        ← Cấu hình Security, Demo data
│   │   │   ├── post/          ← Bài đăng (entity, service, controller)
│   │   │   ├── reaction/      ← Phản ứng bài đăng
│   │   │   └── user/          ← Người dùng
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/migration/  ← Flyway migration scripts
│   └── test/
│       ├── java/vn/edu/ptit/campushub/
│       │   └── PostApiIntegrationTest.java
│       └── resources/
│           └── application.yml   ← H2 in-memory cho test
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
| API Posts | 8080 | http://localhost:8080/api/posts |
| MySQL   | 3306 | localhost:3306 (internal: `mysql:3306`) |

---

## Biến môi trường

Backend nhận cấu hình kết nối MySQL qua biến môi trường (được định nghĩa trong `compose.yml`):

| Biến | Giá trị trong Compose | Mô tả |
|------|-----------------------|-------|
| `DB_URL` | `jdbc:mysql://mysql:3306/campus_hub?createDatabaseIfNotExist=true` | URL kết nối — dùng tên service `mysql` thay `localhost` |
| `DB_USERNAME` | `campushub` | Username MySQL |
| `DB_PASSWORD` | `campushub123` | Password MySQL |
| `SERVER_PORT` | `8080` | Cổng backend |

---
