(function () {
    "use strict";

    var app = angular.module('App');

    app.service('AuthGuard', function ($state, AuthService, LOGIN_STATE) {
        var self = this;

        self.canActivate = function () {
            var result = false;
            if (!AuthService.isAuthenticated()) {
                $state.go(LOGIN_STATE);
            } else {
                result = true;
            }
            return result;
        };
    });
})();
