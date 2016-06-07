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
            'delete': { method:'DELETE' },
            'update': { method:'PUT' },
            'save': { method:'POST' }
        });
    }
})();

(function() {
    'use strict';
    angular
        .module('app.terraSeller')
        .factory('InventLocationImport', InventLocationImport);

    InventLocationImport.$inject = ['$resource'];

    function InventLocationImport ($resource) {
        var resourceUrl =  'api/invent-locations/import';

        return $resource(resourceUrl, {}, {
            'save': {
                method:'POST',
                isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }}
        });
    }
})();

(function() {
    'use strict';
    angular
        .module('app.terraSeller')
        .factory('InventLocationUpdateAll', InventLocationUpdateAll);

    InventLocationUpdateAll.$inject = ['$resource'];

    function InventLocationUpdateAll ($resource) {
        var resourceUrl =  'api/invent-locations/update';

        return $resource(resourceUrl, {}, {
            'save': {
                method:'PUT',
                isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }}
        });
    }
})();
