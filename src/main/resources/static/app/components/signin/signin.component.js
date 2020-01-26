(function () {
    "use strict";

    var app = angular.module('App');

    app.component('signin.component', {
        templateUrl: '/app/components/signin/signin.component.html',
        controllerAs: 'ctrl',
        controller: function (AuthService, $state, toastr, STATE) {
            var ctrl = this;
            ctrl.credentials = {};
            ctrl.state = 'ready';
            
            ctrl.$onInit = function () {
                ctrl.state = 'ready';
                if (!AuthService.isAuthenticated()) {
                    ctrl.state = 'waiting';
                    AuthService.obtainProfile().then(function () {
                        ctrl.state = 'ready';
                        $state.go(STATE.HOME);
                    }, function () {
                        ctrl.state = 'ready';
                    });
                }
            };

            ctrl.signIn = function () {
                if (ctrl.credentials.username && ctrl.credentials.password) {
                    ctrl.state = 'waiting';
                    var credentials = {
                        username: ctrl.credentials.username, 
                        password: ctrl.credentials.password
                    };
                    AuthService.signIn(credentials).then(function () {
                        $state.go(STATE.HOME);
                    }, function () {
                        ctrl.state = 'ready';
                        ctrl.credentials.password = "";
                        toastr.error("Usuario o contraseña no valido", "Error");
                    });
                }
            };
            
            ctrl.goToSignUp = function () {
                $state.go(STATE.SIGN_UP);
            };
        }});
})();