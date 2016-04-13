(function() {
    'use strict';

    angular
        .module('app.jHipster')
        .controller('JhiLanguageController', JhiLanguageController);

    JhiLanguageController.$inject = ['$translate', 'JhiLanguageService', 'tmhDynamicLocale'];

    function JhiLanguageController ($translate, JhiLanguageService, tmhDynamicLocale) {
        var vm = this;

        vm.changeLanguage = changeLanguage;
        vm.getLanguageFromKey = getLanguageFromKey;
        vm.languages = null;
        vm.currentLanguage = {
            key: '',
            title: '',
            alt: ''
        };

        function getLanguageFromKey(lang) {
            return {
                'ca': 'Català',
                'da': 'Dansk',
                'de': 'Deutsch',
                'el': 'Ελληνικά',
                'en': 'English',
                'es': 'Español',
                'fr': 'Français',
                'gl': 'Galego',
                'hu': 'Magyar',
                'hi': 'हिंदी',
                'it': 'Italiano',
                'ja': '日本語',
                'ko': '한국어',
                'mr': 'मराठी',
                'nl': 'Nederlands',
                'pl': 'Polski',
                'pt-br': 'Português (Brasil)',
                'pt-pt': 'Português',
                'ro': 'Română',
                'ru': 'Русский',
                'sv': 'Svenska',
                'ta': 'தமிழ்',
                'tr': 'Türkçe',
                'zh-cn': '中文（简体）',
                'zh-tw': '繁體中文'
            }[lang];
        }

        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        JhiLanguageService.getCurrent().then(function (languageKey) {
            vm.currentLanguage.key = languageKey;
            vm.currentLanguage.title = getLanguageFromKey(languageKey);
            vm.currentLanguage.alt = vm.currentLanguage.title;
        });

        function changeLanguage (languageKey) {
            $translate.use(languageKey);
            tmhDynamicLocale.set(languageKey);
            vm.currentLanguage.key = languageKey;
            vm.currentLanguage.title = getLanguageFromKey(languageKey);
            vm.currentLanguage.alt = vm.currentLanguage.title;
        }
    }
})();
