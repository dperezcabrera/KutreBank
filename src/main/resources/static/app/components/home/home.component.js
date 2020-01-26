(function () {
    "use strict";

    var app = angular.module('App');

    app.component('home.component', {
        templateUrl: '/app/components/home/home.component.html',
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
        }});
})();
