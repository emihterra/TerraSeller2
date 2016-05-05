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
        var service = $resource('api/usersettings/:login', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });

        return service;

    }
})();

