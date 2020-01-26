(function () {
    "use strict";

    var app = angular.module('App');

    app.component('signup.component', {
        templateUrl: '/app/components/signup/signup.component.html',
        controllerAs: 'ctrl',
        controller: function (AuthService, $state, toastr, STATE) {
            var ctrl = this;
            ctrl.credentials = {};
            ctrl.state = 'ready';
            ctrl.signupData = {};
            ctrl.error = {};
            ctrl.teams = [];

            ctrl.$onInit = function () {
                ctrl.state = 'waiting';
                if (AuthService.isAuthenticated()) {
                    $state.go(STATE.HOME);
                    ctrl.state = 'ready';
                } else {
                    AuthService.obtainTeams().then(function (response) {
                        ctrl.teams = response.data;
                        ctrl.state = 'ready';
                    }, function () {
                        toastr.error("No se han podido recuperar los equipos", "Error");
                    });
                }
            };
            
            function isEmpty(str){
                return str === undefined || str === null || str === "";
            }
            
            function checkData() {
                ctrl.error = {};
                if (isEmpty(ctrl.signupData.team)) {
                    toastr.error("No ha seleccionado su equipo", "Error");
                    ctrl.error.team = true;
                } else if (isEmpty(ctrl.signupData.username)) {
                    toastr.error("El nombre de usuario no puede ser vacío", "Error");
                    ctrl.error.username = true;
                } else if (isEmpty(ctrl.signupData.password)) {
                    toastr.error("La contraseña no puede estar vacía");
                    ctrl.error.password = true;
                } else if (ctrl.signupData.password !== ctrl.signupData.password2) {
                    toastr.error("Las contraseñas no son iguales", "Error");
                    ctrl.error.password = true;
                    ctrl.error.password2 = true;
                }
                return angular.equals(ctrl.error, {});
            }

            ctrl.signUp = function () {
                if (checkData()) {
                    ctrl.state = 'waiting';
                    var signUpData = {
                        username: ctrl.signupData.username,
                        password: ctrl.signupData.password,
                        team: ctrl.signupData.team
                    };
                    AuthService.signUp(signUpData).then(function (response) {
                        toastr.info(response.data.description, "Información");
                        $state.go(STATE.SIGN_IN);
                    }, function (error) {
                        ctrl.signupData.error = error.data.description;
                        toastr.error(error.data.description, "Error");
                        ctrl.state = 'ready';
                    });
                }
            };

            ctrl.goToSignIn = function () {
                $state.go(STATE.SIGN_IN);
            };
        }});
})();
