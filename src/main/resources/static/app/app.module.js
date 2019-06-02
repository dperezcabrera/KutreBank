(function () {
    "use strict";

    var app = angular.module('App', ['ngRoute', 'ngResource']);
    
    app.run(function ($http) {
        $http.defaults.headers.common.version = '@project.version@';
    });
    
})();

// Global namespace
var App = {common: {}};