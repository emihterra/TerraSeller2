(function() {
    'use strict';
    angular
        .module('app.terraSeller')
        .factory('ClientRoomItem', ClientRoomItem);

    ClientRoomItem.$inject = ['$resource'];

    function ClientRoomItem ($resource) {
        var resourceUrl =  'api/client-room-items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' }
        });
    }
})();
