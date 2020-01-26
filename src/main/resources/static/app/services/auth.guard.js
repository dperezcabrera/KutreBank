(function () {
    "use strict";

    var app = angular.module('App');

    app.service('AuthGuard', function ($state, AuthService, STATE) {
        var self = this;

        self.canActivate = function () {
            var result = false;
            if (!AuthService.isAuthenticated()) {
                $state.go(STATE.SIGN_IN);
            } else {
                result = true;
            }
            return result;
        };
    });
})();
