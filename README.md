# **Restaurant Order Management API**

## **Overview**
The **Restaurant API** is a scalable, secure, and well-structured system for managing restaurant orders. It includes authentication, role-based access control, rate limiting, pagination, filtering, and best practices to ensure high performance and security. This API is designed for real-world implementation, making it suitable for freelance projects and production-level applications.

## **Tech Stack & Best Practices**
- **Security**: JWT Authentication, Role-Based Access Control (RBAC)
- **Scalability**: Rate Limiting, Pagination, Caching, API Versioning
- **Performance**: Query Optimization, Sorting & Filtering, Indexing
- **Monitoring & Logging**: Request Logging, Error Handling, Real-time Monitoring
- **Robustness**: Idempotent Requests, Validation, Webhooks

## **Roles & Responsibilities**
| Role          | Responsibilities |
|--------------|----------------|
| **Customer** | Browse menu, manage cart, place orders, track orders |
| **Manager**  | Manage orders, assign delivery crew, update statuses, manage menu items |
| **Delivery Crew** | View assigned orders, update delivery status |

## **Endpoints**

## **Advanced Features & Best Practices**
### ✅ **Rate Limiting & Security**
- Uses API gateway or middleware to prevent abuse.
- Limits requests per user to prevent spamming.
### **1. Authentication & User Management**
| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/api/v1/auth/register` | `POST` | Registers a new user |
| `/api/v1/auth/login` | `POST` | Logs in and returns a JWT token |
| `/api/v1/auth/logout` | `POST` | Logs out the user |
| `/api/v1/users/profile` | `GET` | Fetches user profile details |
| `/api/v1/users` | `GET` | (Admin) List all users |
| `/api/v1/users/{userId}` | `PATCH` | (Admin) Updates a user role |

### **2. Order Endpoints**
| Endpoint | Role | Method | Purpose |
|----------|------|--------|---------|
| `/api/v1/orders` | Customer | `GET @PreAuthorize("hasRole('MANAGER')")` | Fetch paginated orders with filtering & sorting |
| `/api/v1/orders` | Customer | `POST` | Places an order from the cart (Idempotent) |
| `/api/v1/orders/{orderId}` | Customer | `GET` | Fetches order details (403 if unauthorized) |
| `/api/v1/orders` | Manager | `GET` | Retrieves all orders (Paginated) |
| `/api/v1/orders/{orderId}` | Manager | `PUT` | Updates order (Assign crew, update status) |
| `/api/v1/orders/{orderId}` | Manager | `DELETE` | Cancels an order |
| `/api/v1/orders` | Delivery Crew | `GET` | Fetch assigned orders |
| `/api/v1/orders/{orderId}` | Delivery Crew | `PATCH` | Updates order status (0 = Out for delivery, 1 = Delivered) |

### **3. Cart Endpoints**
| Endpoint | Role | Method | Purpose |
|----------|------|--------|---------|
| `/api/v1/cart` | Customer | `GET` | Retrieves all cart items (Paginated) |
| `/api/v1/cart` | Customer | `POST` | Adds an item to the cart |
| `/api/v1/cart/{itemId}` | Customer | `DELETE` | Removes an item from the cart |
| `/api/v1/cart` | Customer | `DELETE` | Clears all cart items |

### **4. Menu Item Endpoints**
| Endpoint | Role | Method | Purpose |
|----------|------|--------|---------|
| `/api/v1/menu` | Customer | `GET` | Retrieves available menu items (Paginated, Filtered, Sorted) |
| `/api/v1/menu/{itemId}` | Customer | `GET` | Retrieves a specific menu item |
| `/api/v1/menu` | Manager | `POST` | Adds a new menu item |
| `/api/v1/menu/{itemId}` | Manager | `PUT` | Updates an existing menu item |
| `/api/v1/menu/{itemId}` | Manager | `DELETE` | Deletes a menu item |

### **5. Webhooks & Notifications**
| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/api/v1/webhooks/orders` | `POST` | Sends real-time order status updates |
| `/api/v1/notifications` | `GET` | Fetches user notifications |


### ✅ **Pagination, Filtering & Sorting**
- Orders & menu items support **limit, offset, sorting, filtering**.
- Example: `/api/v1/menu?category=drinks&sort=price_desc&page=1&limit=10`

### ✅ **Caching**
- Frequently accessed menu items & orders are cached.
- Uses **Redis** or in-memory caching for quick retrieval.

### ✅ **Idempotency for Order Submission**
- Orders use **Idempotency keys** to prevent duplicate submissions.

### ✅ **JWT Authentication & Role-Based Access Control (RBAC)**
- JWT tokens for secure authentication.
- Users have roles (`customer`, `manager`, `delivery_crew`).

### ✅ **Logging & Monitoring**
- **Structured logging** for API requests/responses.
- **Error monitoring** (e.g., Sentry, Prometheus, CloudWatch).

## **Status Codes & Error Handling**
| Code | Meaning |
|------|---------|
| 200  | Success |
| 201  | Created |
| 400  | Bad Request |
| 401  | Unauthorized |
| 403  | Forbidden |
| 404  | Not Found |
| 429  | Too Many Requests (Rate Limited) |
| 500  | Internal Server Error |

## **Example API Calls**
### **1. Place a New Order** (Idempotent)
**Request:**
```json
POST /api/v1/orders
Headers: {"Idempotency-Key": "abcd-1234"}
{
  "cartId": 123
}
```
**Response:**
```json
{
  "orderId": 456,
  "status": "Pending",
  "items": [
    { "name": "Pizza", "quantity": 2 },
    { "name": "Cola", "quantity": 1 }
  ]
}
```

### **2. Fetch Orders with Pagination & Filtering**
**Request:**
```http
GET /api/v1/orders?page=1&limit=10&status=pending&sort=created_at_desc
```
**Response:**
```json
{
  "total": 100,
  "page": 1,
  "limit": 10,
  "orders": [
    { "orderId": 456, "status": "Pending", "items": 3 },
    { "orderId": 457, "status": "Pending", "items": 2 }
  ]
}
```

## **Final Notes**
This API is designed for **real-world applications**, following best practices for scalability, security, and performance.
