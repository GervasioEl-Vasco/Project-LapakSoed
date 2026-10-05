
| Method | Endpoint | Keterangan |
|---|---|---|
| POST | `/api/v1/auth/register` | Daftar dengan `email`, `password`, `fullName`, `nim` |
| POST | `/api/v1/auth/login` | Login dan memperoleh JWT |
| GET | `/api/v1/auth/me` | **auth** — profil akun |
| GET | `/api/v1/listings` | Daftar barang, query opsional `q`, `category`, `minPrice`, `maxPrice`, `page`, `size` |
| GET | `/api/v1/listings/{id}` | Detail barang |
| GET | `/api/v1/listings/mine` | **auth** — barang milik akun |
| POST | `/api/v1/listings` | **auth** — membuat listing |
| PUT | `/api/v1/listings/{id}` | **auth** — mengubah listing milik sendiri |
| PATCH | `/api/v1/listings/{id}/status` | **auth** — `AVAILABLE`, `RESERVED`, atau `SOLD` |
| DELETE | `/api/v1/listings/{id}` | **auth** — menghapus listing milik sendiri |
| GET | `/api/v1/favorites` | **auth** — daftar barang tersimpan |
| POST/DELETE | `/api/v1/favorites/{listingId}` | **auth** — simpan/hapus barang tersimpan |
| POST | `/api/v1/conversations` | **auth** — mulai chat, body `{ "listingId": "..." }` |
| GET | `/api/v1/conversations` | **auth** — daftar chat |
| GET | `/api/v1/conversations/{id}/messages` | **auth** — riwayat chat |
| POST | `/api/v1/conversations/{id}/messages` | **auth** — kirim pesan dengan body `{ "body": "..." }` |
| POST | `/api/v1/orders` | **auth** — buat pesanan barang dengan `listingId`, `quantity: 1`, dan `paymentMethod` |
| GET | `/api/v1/orders/mine` | **auth** — riwayat pesanan sebagai pembeli/penjual |
| PATCH | `/api/v1/orders/{orderId}/status` | **auth** — transisi status `NEW`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED` sesuai izin |
| POST | `/api/v1/service-requests` | **auth** — ajukan servis perangkat dan pilih metode pembayaran |
| GET | `/api/v1/service-requests/mine` | **auth** — riwayat permintaan jasa servis |


