(function () {
    "use strict";

    var app = angular.module('App');

    app.service('credentials', function () {
        this.fromView = "/home";

        this.user = {};

        this.listeners = [];

        this.registerUser = function (user) {
            this.user = user;
            this.listeners.forEach(function (listener) {
                listener({event: 'authenticated', username: user.username});
            });
        };

        this.registerAuthenticationListener = function (callback) {
            if (callback) {
                this.listeners.push(callback);
            }
        };

        this.logout = function () {
            var username = this.user.username;
            this.user = {};
            this.listeners.forEach(function (listener) {
                listener({event: 'logout', username: username});
            });
        };

        this.hasRole = function (role) {
            return this.user.roles && this.user.roles.indexOf(role) >= 0;
        };

        this.hasAnyRole = function (roles) {
            return this.user.roles && this.user.roles.some(function (role) {return roles.indexOf(role) >= 0;});
        };

        this.authenticatedUser = function () {
            return this.user.username;
        };
    });
})();

