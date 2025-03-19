# Rating System API Documentation

This document provides a comprehensive overview of all available endpoints in the Rating System API.

## Authentication Endpoints

### `/auth/signup`
- **Method:** POST
- **Description:** Register a new user account
- **Request Body:** `SignUpRequestDTO`
- **Response:** `SignUpResponseDTO` (Status: 201 Created)

### `/auth/verify`
- **Method:** GET
- **Description:** Verify email address
- **Query Parameters:**
    - `token`: Email verification token
- **Response:** `EmailVerificationDTO`

### `/auth/login`
- **Method:** POST
- **Description:** Authenticate and login user
- **Request Body:** `LoginRequestDTO`
- **Response:** `LoginResponseDTO`

### `/auth/forgot_password/{email}`
- **Method:** GET
- **Description:** Initiate password recovery process
- **Path Variables:**
    - `email`: User's email address
- **Response:** `ForgotPasswordResponseDTO`

### `/auth/reset_password`
- **Method:** POST
- **Description:** Reset user password
- **Request Body:** `ResetPasswordRequestDTO`
- **Response:** `ResetPasswordResponseDTO`

## Admin Endpoints

### `/admin/seller/{id}/approve`
- **Method:** POST
- **Description:** Approve a seller registration
- **Path Variables:**
    - `id`: Seller ID to approve
- **Response:** String message (Status: 200 OK)

### `/admin/seller/{id}/reject`
- **Method:** POST
- **Description:** Reject a seller registration
- **Path Variables:**
    - `id`: Seller ID to reject
- **Response:** String message (Status: 200 OK)

### `/admin/comment`
- **Method:** POST
- **Description:** Approve a comment
- **Response:** String message (Status: 200 OK)

## Game Object Endpoints

### `/object/{objectId}`
- **Method:** GET
- **Description:** Get a game object by its ID
- **Path Variables:**
    - `objectId`: Game object ID
- **Response:** `GameObjectDTO` (Status: 200 OK)

### `/object`
- **Method:** POST
- **Description:** Create a new game object
- **Request Body:** `GameObjectRequestDTO`
- **Headers:**
    - `Authorization`: Bearer token
- **Response:** `GameObjectDTO` (Status: 200 OK)

### `/object/{objectId}`
- **Method:** PUT
- **Description:** Update an existing game object
- **Path Variables:**
    - `objectId`: Game object ID to update
- **Request Body:** `GameObjectRequestDTO`
- **Headers:**
    - `Authorization`: Bearer token
- **Response:** `GameObjectDTO` (Status: 200 OK)

### `/object/{objectId}`
- **Method:** DELETE
- **Description:** Delete a game object
- **Path Variables:**
    - `objectId`: Game object ID to delete
- **Headers:**
    - `Authorization`: Bearer token
- **Response:** No content (Status: 200 OK)

## User Endpoints

### `/users/{userId}`
- **Method:** GET
- **Description:** Get user information by ID
- **Path Variables:**
    - `userId`: User ID
- **Response:** `UserDTO` (Status: 200 OK)

### `/users/{sellerId}/comments`
- **Method:** POST
- **Description:** Add a comment for a seller
- **Path Variables:**
    - `sellerId`: Seller ID
- **Request Body:** `CommentRequestDTO`
- **Headers:**
    - `Authorization`: Bearer token (optional)
- **Response:** `CommentResponseDTO` (Status: 200 OK)

### `/users/{sellerId}/comments`
- **Method:** GET
- **Description:** Get all comments for a seller
- **Path Variables:**
    - `sellerId`: Seller ID
- **Response:** List of `CommentDTO` (Status: 200 OK)

### `/users/comments/{commentId}`
- **Method:** GET
- **Description:** Get a comment by ID
- **Path Variables:**
    - `commentId`: Comment ID
- **Response:** `CommentDTO` (Status: 200 OK)

### `/users/comments/{commentId}`
- **Method:** DELETE
- **Description:** Delete a comment
- **Path Variables:**
    - `commentId`: Comment ID
- **Headers:**
    - `Authorization`: Bearer token
- **Response:** No content (Status: 200 OK)

### `/users/comments/{commentId}`
- **Method:** PUT
- **Description:** Update a comment
- **Path Variables:**
    - `commentId`: Comment ID
- **Request Body:** `CommentRequestDTO`
- **Headers:**
    - `Authorization`: Bearer token
- **Response:** `CommentResponseDTO` (Status: 200 OK)
