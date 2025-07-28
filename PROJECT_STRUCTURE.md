# Plant App - Firebase MVVM Architecture

## 📁 Clean Project Structure

```
app/src/main/java/com/example/plantapp/
├── MainActivity.kt                    # Main entry point
├── PlantApplication.kt                # Hilt Application class
│
├── data/                              # Data Layer
│   ├── model/                         # Data Models
│   │   ├── Plant.kt                   # Plant entity
│   │   ├── User.kt                    # User entity
│   │   ├── CartItem.kt                # Cart item entity
│   │   ├── Order.kt                   # Order entity
│   │   └── OrderItem.kt               # Order item entity
│   │
│   └── repository/                    # Repository Layer
│       ├── PlantRepository.kt         # Firebase plant operations
│       ├── UserRepository.kt          # Firebase auth & user operations
│       ├── CartRepository.kt          # Firebase cart operations
│       └── OrderRepository.kt         # Firebase order operations
│
├── di/                                # Dependency Injection
│   └── FirebaseModule.kt              # Firebase service providers
│
├── ui/                                # UI Layer
│   ├── components/                    # Reusable UI Components
│   │   ├── PlantCard.kt               # Plant display card
│   │   ├── CartItemCard.kt            # Cart item display card
│   │   └── OrderCard.kt               # Order display card
│   │
│   ├── screens/                       # Screen Composables
│   │   ├── LoginScreen.kt             # User login screen
│   │   ├── RegisterScreen.kt          # User registration screen
│   │   ├── ForgotPasswordScreen.kt    # Password reset screen
│   │   ├── HomeScreen.kt              # Plant listing screen
│   │   ├── CartScreen.kt              # Shopping cart screen
│   │   ├── CheckoutScreen.kt          # Order checkout screen
│   │   └── OrdersScreen.kt            # Order history screen
│   │
│   ├── viewmodel/                     # ViewModel Layer
│   │   ├── AuthViewModel.kt           # Authentication logic
│   │   ├── PlantListViewModel.kt      # Plant listing & filtering
│   │   ├── CartViewModel.kt           # Cart management
│   │   └── OrderViewModel.kt          # Order management
│   │
│   └── navigation/                    # Navigation
│       ├── NavGraph.kt                # Navigation setup
│       └── Screen.kt                  # Screen routes
│
└── ui/theme/                          # UI Theme
    ├── Color.kt                       # Color definitions
    ├── Theme.kt                       # App theme
    └── Type.kt                        # Typography
```

## 🏗️ MVVM Architecture Layers

### **Data Layer**
- **Models**: Pure data classes for Firebase entities
- **Repositories**: Firebase operations abstraction
- **No Database Folder**: Using Firebase instead of local database

### **Domain Layer** (Implicit)
- **ViewModels**: Business logic and state management
- **Use Cases**: Business operations (handled in ViewModels)

### **Presentation Layer**
- **Screens**: UI composables
- **Components**: Reusable UI elements
- **Navigation**: Screen routing

### **Dependency Injection**
- **FirebaseModule**: Firebase service providers
- **Hilt**: Dependency injection framework

## 🔥 Firebase Integration

### **Services Used**
- **Firebase Auth**: User authentication
- **Firestore**: NoSQL database
- **Firebase Storage**: Image storage (ready for implementation)

### **Collections Structure**
```
firestore/
├── users/                 # User profiles
├── plants/                # Plant catalog
├── cart/                  # Shopping cart items
├── orders/                # User orders
└── orderItems/            # Order line items
```

## ✅ Clean Architecture Benefits

1. **Separation of Concerns**: Each layer has a specific responsibility
2. **Testability**: Easy to unit test each component
3. **Maintainability**: Clear structure makes code easy to maintain
4. **Scalability**: Easy to add new features
5. **No Database Dependencies**: Pure Firebase implementation

## 🚀 Key Features

- ✅ User Authentication (Register/Login/Logout)
- ✅ Plant Catalog with Filtering
- ✅ Shopping Cart Management
- ✅ Order Processing
- ✅ Order History
- ✅ Real-time Data Sync
- ✅ Modern UI with Material Design 3 