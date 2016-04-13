(function() {
    'use strict';

    angular
        .module('app.jHipster')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('jhi-configuration', {
            parent: 'admin',
            url: '/configuration',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'configuration.title',
                title: 'Администратор / Параметры'
            },
            views: {
                'content@app': {
                    templateUrl: 'app/jHipster/admin/configuration/configuration.html',
                    controller: 'JhiConfigurationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('configuration');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
