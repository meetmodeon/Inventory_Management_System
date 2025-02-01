# 📦 **Inventory Management System**

## **Overview**  
This is a role-based **Inventory Management System** where:  
- **Admin** 🛡️ can **view all ports** and manage the system.  
- **Manager** 👨‍💼 can **view only specific ports** assigned to them.  
- **Spring Security** 🔒 ensures secure API access.  
- **NGX-Charts** 📊 provides data visualization in the frontend.  

---

## 🛠️ **Tech Stack**  

### **Frontend** (🔷 Angular)  
- **Angular** 🌐 (for UI development)  
- **NGX-Charts** 📊 (for interactive charts)  
- **Angular Material** 🎨 (for a responsive UI)  
- **RxJS** 🔄 (for handling API calls)  

### **Backend** (🟡 Spring Boot)  
- **Spring Boot** 🚀 (for RESTful APIs)  
- **Spring Security** 🔐 (for authentication & authorization)  
- **JWT (JSON Web Token)** 🪪 (for secure token-based access)  
- **Spring Data JPA** 🗄️ (for database interaction)  
- **MySQL / PostgreSQL** 🗃️ (for data storage)  

---

## 🔑 **Roles & Permissions**  
| Role    | Permissions 🚀 |
|---------|--------------|
| **Admin** 🛡️ | ✅ View & Manage all ports, users, inventory items |
| **Manager** 👨‍💼 | ✅ View only assigned ports, manage limited inventory |

---

## 🎯 **Features**  
✅ **Role-Based Access** (Admin & Manager)  
✅ **Secure API Calls** using **JWT Authentication** 🔑  
✅ **Manage Inventory** 🏭 (Add, Update, Delete, View Items)  
✅ **Port-Specific Access** ⚓ (Manager sees only assigned ports)  
✅ **Data Visualization** 📊 using **NGX-Charts**  
✅ **Responsive UI** 🎨 with **Angular Material**  
✅ **Database Integration** 🗄️ (Spring Boot + MySQL/PostgreSQL)  

---

## 🚀 **Installation & Setup**  

### **1️⃣ Backend Setup (Spring Boot)**
#### 📌 **Clone the Repository**
```bash
git clone https://github.com/meetmodeon/Inventory_Management_System.git
cd inventory-management/backend
