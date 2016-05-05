(function() {
    'use strict';

    angular
        .module('app.jHipster')
        .controller('RegisterController', RegisterController);


    RegisterController.$inject = ['$translate', '$timeout', 'Auth', 'LoginService', 'terraSellerDimensionService', 'terraSellerSettingsService'];

    function RegisterController ($translate, $timeout, Auth, LoginService, terraSellerDimensionService, terraSellerSettingsService) {
        var vm = this;

        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.registerAccount = {};
        vm.success = null;
        vm.dimensions = [];
        vm.emplSettings = {
            login: "",
            emplcode: "",
            dimension: "",
            lastClientCode: ""
        };

        $timeout(function (){angular.element('[ng-model="vm.registerAccount.login"]').focus();});

        function register () {
            if (vm.registerAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = 'ERROR';
            } else {
                vm.registerAccount.langKey = $translate.use();
                vm.doNotMatch = null;
                vm.error = null;
                vm.errorUserExists = null;
                vm.errorEmailExists = null;

                Auth.createAccount(vm.registerAccount).then(function () {
                    vm.success = 'OK';
                    vm.emplSettings.login = vm.registerAccount.login;
                    terraSellerSettingsService.save(vm.emplSettings);
                }).catch(function (response) {
                    vm.success = null;
                    if (response.status === 400 && response.data === 'login already in use') {
                        vm.errorUserExists = 'ERROR';
                    } else if (response.status === 400 && response.data === 'e-mail address already in use') {
                        vm.errorEmailExists = 'ERROR';
                    } else {
                        vm.error = 'ERROR';
                    }
                });
            }
        }

        terraSellerDimensionService.get().then(function(response) {
            vm.dimensions = response;
        });

    }
})();
