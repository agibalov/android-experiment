package me.loki2302.dal.dto;

public enum ServiceError {
    InternalError,
    NoSuchUser,
    InvalidPassword,
    UserAlreadyRegistered,
    SessionExpired,
    NoSuchTask,
    NoPermissions,
    InvalidTaskStatus,
    ValidationError,
    NoSuchPendingUser,
    NoSuchPendingPasswordResetRequest
}