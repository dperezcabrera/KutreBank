(function () {
    "use strict";

    var app = angular.module('App');

    app.service('MovementService', function ($http) {
        var self = this;
        var MOVEMENTS_URL = "/movements";
        var TRANSFER_URL = MOVEMENTS_URL + "/transfer";

        self.getMovements = function () {
            return $http.get(MOVEMENTS_URL);
        };

        self.transfer = function (transference) {
            return $http.post(TRANSFER_URL, transference);
        };
    });
})();
