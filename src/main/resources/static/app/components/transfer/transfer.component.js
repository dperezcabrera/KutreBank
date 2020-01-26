(function () {
    "use strict";

    var app = angular.module('App');

    app.component('transfer.component', {
        templateUrl: '/app/components/transfer/transfer.component.html',
        controllerAs: 'ctrl',
        controller: function (MovementService, AuthService, $state, toastr, STATE) {
            var ctrl = this;
            ctrl.state = 'waiting';
            ctrl.user = {};
            ctrl.transferData = {};
            ctrl.movements = [];

            ctrl.$onInit = function () {
                ctrl.user = AuthService.getUser();
                MovementService.getMovements().then(function (response) {
                    var amount = 0;
                    ctrl.movements = response.data.map(function (m) {
                        if (m.originId === ctrl.user.id) {
                            m.amount = -m.amount;
                        }
                        amount += m.amount;
                        return m;
                    });
                    ctrl.user.amount = amount;
                    ctrl.state = 'ready';
                });
            };

            ctrl.transfer = function () {
                MovementService.transfer(ctrl.transferData).then(function (response) {
                    toastr.info(response.data.description, "Información");
                    ctrl.user.amount = ctrl.user.amount - ctrl.transferData.amount;
                    ctrl.$onInit();
                }, function (error) {
                    toastr.error(error.data.description, "Error");
                });
            };

            ctrl.logout = function () {
                AuthService.logout();
                $state.go(STATE.SIGN_IN);
            };
        }});
})();
