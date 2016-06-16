/**
 * Created by emih on 29.04.2016.
 */

(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .factory('terraSellerSettingsService', terraSellerSettingsService);

    terraSellerSettingsService.$inject = ['$resource', '$http'];

    function terraSellerSettingsService ($resource, $http) {

        var service = {
            get: get,
            update: update,
            save: save,
            getDefault: getDefault
        };

        return service;

        function get(login){
            return $http.get('api/usersettings/' + login)
                .then(getSettingsComplete);

            function getSettingsComplete (response) {
                return response.data;
            }
        };

        function update(settings){
            return $http.put('api/usersettings', settings)
                .then(getSettingsComplete);

            function getSettingsComplete (response) {
                return response.data;
            }
        };

        function save(settings){
            return $http.post('api/usersettings', settings)
                .then(getSettingsComplete);

            function getSettingsComplete (response) {
                return response.data;
            }
        };

        function data() {
            var serv = $resource('api/usersettings/:login', {}, {
                'query': {method: 'GET', isArray: true},
                'get': {
                    method: 'GET',
                    transformResponse: function (response) {
                        var res = {};
                        if(response) {
                            angular.fromJson(response);
                        }
                        return res;
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

