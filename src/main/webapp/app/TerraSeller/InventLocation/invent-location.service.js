(function() {
    'use strict';
    angular
        .module('app.terraSeller')
        .factory('InventLocation', InventLocation);

    InventLocation.$inject = ['$resource'];

    function InventLocation ($resource) {
        var resourceUrl =  'api/invent-locations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' }
        });
    }
})();
