/**
 * Created by emih on 29.04.2016.
 */

(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .factory('terraSellerSettingsService', terraSellerSettingsService);

    terraSellerSettingsService.$inject = ['$resource'];

    function terraSellerSettingsService ($resource) {

        var service = {
            data: data,
            getDefault: getDefault
        };

        return service;

        function data() {
            var serv = $resource('api/usersettings/:login', {}, {
                'query': {method: 'GET', isArray: true},
                'get': {
                    method: 'GET',
                    transformResponse: function (response) {
                        response = angular.fromJson(response);
                        return response;
                    }
                },
                'save': {method: 'POST'},
                'update': {method: 'PUT'},
                'delete': {method: 'DELETE'}
            });
            return serv;
        }

        function getDefault() {
            var emplSettings = {
                login: "",
                emplcode: "",
                dimension: "",
                lastClientCode: "",
                useDefaultClient: true
            };
            return emplSettings;
        }

    }
})();

