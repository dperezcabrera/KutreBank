(function () {
    "use strict";

    var app = angular.module('App');

    app.filter('pagination', function () {
        return function (input, pagination, calculatePagination) {
            if (calculatePagination) {
                pagination.updateItems(input.length);
            }
            var begin = (pagination.page - 1) * pagination.itemsPerPage;
            return input.slice(begin, begin + pagination.itemsPerPage);
        };
    });
})();

