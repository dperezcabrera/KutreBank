(function () {
    "use strict";

    var app = angular.module('App');

    app.service('AuthService', function ($q, $http) {
        var self = this;

        var LOGIN_REST_URL = "/auth/login";
        var LOGOUT_REST_URL = "/auth/logout";
        var SIGNUP_REST_URL = "/auth/signup";
        var TEAMS_REST_URL = "/teams";

        self.authenticated = false;
        self.user = {username: 'unknown'};

        function login(promise) {
            var deferred = $q.defer();
            promise.then(function (response) {
                self.user = response.data;
                self.authenticated = true;
                deferred.resolve();
            }, function (error) {
                deferred.reject(error.data);
            });
            return deferred.promise;
        }

        self.signIn = function (user) {
            return login($http.post(LOGIN_REST_URL, user));
        };

        self.obtainProfile = function () {
            return login($http.get(LOGIN_REST_URL));
        };
        
        self.obtainTeams = function () {
            return $http.get(TEAMS_REST_URL);
        };

        function logout() {
            self.user = {};
            self.authenticated = false;
        }

        self.logout = function () {
            if (self.authenticated) {
                $http.post(LOGOUT_REST_URL);
                logout();
            }
        };

        self.onError = function () {
            if (self.authenticated) {
                logout();
            }
        };

        self.signUp = function (user) {
            return $http.post(SIGNUP_REST_URL, user);
        };

        self.isAuthenticated = function () {
            return self.authenticated;
        };

        self.getUser = function () {
            return angular.copy(self.user);
        };
    });
})();
