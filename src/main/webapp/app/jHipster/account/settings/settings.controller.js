(function() {
    'use strict';

    angular
        .module('app.jHipster')
        .controller('SettingsController', SettingsController);

    SettingsController.$inject = ['Principal', 'Auth', 'JhiLanguageService', '$translate', 'terraSellerDimensionService', 'terraSellerSettingsService'];

    function SettingsController (Principal, Auth, JhiLanguageService, $translate, terraSellerDimensionService, terraSellerSettingsService) {
        var vm = this;

        vm.error = null;
        vm.save = save;
        vm.settingsAccount = null;
        vm.success = null;
        vm.dimensions = [];
        vm.emplSettings = {
            login: "",
            emplcode: "",
            dimension: "",
            lastClientCode: ""
        };

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login
            };
        };

        terraSellerDimensionService.get().then(function(response) {
            vm.dimensions = response;
        });

        Principal.identity().then(function(account) {
            vm.settingsAccount = copyAccount(account);
            terraSellerSettingsService.get({login: account.login}, function(result) {
                vm.emplSettings = result;
            });
        });

        function save () {
            Auth.updateAccount(vm.settingsAccount).then(function() {
                vm.error = null;
                vm.success = 'OK';
                vm.emplSettings.login = vm.settingsAccount.login;
                terraSellerSettingsService.update(vm.emplSettings);
                Principal.identity(true).then(function(account) {
                    vm.settingsAccount = copyAccount(account);
                });
                JhiLanguageService.getCurrent().then(function(current) {
                    if (vm.settingsAccount.langKey !== current) {
                        $translate.use(vm.settingsAccount.langKey);
                    }
                });
            }).catch(function() {
                vm.success = null;
                vm.error = 'ERROR';
            });
        }
    }
})();
