// Karma configuration
// http://karma-runner.github.io/0.10/config/configuration-file.html

module.exports = function (config) {
    config.set({
        // base path, that will be used to resolve files and exclude
        basePath: 'src/test/javascript/'.replace(/[^/]+/g,'..'),

        // testing framework to use (jasmine/mocha/qunit/...)
        frameworks: ['jasmine'],

        // list of files / patterns to load in the browser
        files: [
            // bower:js
            'src/main/webapp/bower_components/angular/angular.js',
            'src/main/webapp/bower_components/angular-animate/angular-animate.js',
            'src/main/webapp/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
            'src/main/webapp/bower_components/angular-resource/angular-resource.js',
            'src/main/webapp/bower_components/angular-sanitize/angular-sanitize.js',
            'src/main/webapp/bower_components/angular-ui-router/release/angular-ui-router.js',
            'src/main/webapp/bower_components/angular-xeditable/dist/js/xeditable.js',
            'src/main/webapp/bower_components/jquery/dist/jquery.js',
            'src/main/webapp/bower_components/autotype/index.js',
            'src/main/webapp/bower_components/bootstrap3-fontawesome-timepicker/js/bootstrap-timepicker.min.js',
            'src/main/webapp/bower_components/bootstrap-colorpicker/js/bootstrap-colorpicker.js',
            'src/main/webapp/bower_components/bootstrap-duallistbox/src/jquery.bootstrap-duallistbox.js',
            'src/main/webapp/bower_components/bootstrap-markdown/js/bootstrap-markdown.js',
            'src/main/webapp/bower_components/bootstrap-progressbar/bootstrap-progressbar.js',
            'src/main/webapp/bower_components/bootstrap-tagsinput/dist/bootstrap-tagsinput.js',
            'src/main/webapp/bower_components/bootstrapvalidator/dist/js/bootstrapValidator.js',
            'src/main/webapp/bower_components/clockpicker/dist/jquery-clockpicker.js',
            'src/main/webapp/bower_components/datatables/media/js/jquery.dataTables.js',
            'src/main/webapp/bower_components/datatables-colvis/js/dataTables.colVis.js',
            'src/main/webapp/bower_components/datatables-tabletools/js/dataTables.tableTools.js',
            'src/main/webapp/bower_components/es5-shim/es5-shim.js',
            'src/main/webapp/bower_components/fastclick/lib/fastclick.js',
            'src/main/webapp/bower_components/jquery-1.11.0/dist/jquery.js',
            'src/main/webapp/bower_components/requirejs/require.js',
            'src/main/webapp/bower_components/moment/moment.js',
            'src/main/webapp/bower_components/fuelux/dist/js/fuelux.js',
            'src/main/webapp/bower_components/he/he.js',
            'src/main/webapp/bower_components/ion.rangeSlider/js/ion.rangeSlider.js',
            'src/main/webapp/bower_components/jcrop/js/jquery.Jcrop.js',
            'src/main/webapp/bower_components/jquery.easy-pie-chart/dist/jquery.easypiechart.js',
            'src/main/webapp/bower_components/jquery-form/jquery.form.js',
            'src/main/webapp/bower_components/jquery-knob/js/jquery.knob.js',
            'src/main/webapp/bower_components/jquery-maskedinput/dist/jquery.maskedinput.js',
            'src/main/webapp/bower_components/jquery-ui/jquery-ui.js',
            'src/main/webapp/bower_components/jquery-validation/dist/jquery.validate.js',
            'src/main/webapp/bower_components/json3/lib/json3.js',
            'src/main/webapp/bower_components/lodash/lodash.js',
            'src/main/webapp/bower_components/moment-timezone/builds/moment-timezone-with-data-2010-2020.js',
            'src/main/webapp/bower_components/nouislider/distribute/jquery.nouislider.all.min.js',
            'src/main/webapp/bower_components/relayfoods-jquery.sparkline/dist/jquery.sparkline.min.js',
            'src/main/webapp/bower_components/restangular/dist/restangular.js',
            'src/main/webapp/bower_components/jquery-bridget/jquery-bridget.js',
            'src/main/webapp/bower_components/seiyria-bootstrap-slider/js/bootstrap-slider.js',
            'src/main/webapp/bower_components/select2/select2.js',
            'src/main/webapp/bower_components/summernote/dist/summernote.js',
            'src/main/webapp/bower_components/to-markdown/dist/to-markdown.js',
            'src/main/webapp/bower_components/highcharts/highcharts.js',
            'src/main/webapp/bower_components/highcharts/highcharts-more.js',
            'src/main/webapp/bower_components/highcharts/modules/exporting.js',
            'src/main/webapp/bower_components/highchartTable/jquery.highchartTable.js',
            'src/main/webapp/bower_components/Chart.js/Chart.js',
            'src/main/webapp/bower_components/raphael/raphael.js',
            'src/main/webapp/bower_components/mocha/mocha.js',
            'src/main/webapp/bower_components/morris.js/morris.js',
            'src/main/webapp/bower_components/dygraphs/dygraph-combined.js',
            'src/main/webapp/bower_components/bower-jvectormap/jquery-jvectormap-1.2.2.min.js',
            'src/main/webapp/bower_components/dropzone/dist/min/dropzone.min.js',
            'src/main/webapp/bower_components/angular-simple-logger/dist/angular-simple-logger.js',
            'src/main/webapp/bower_components/angular-google-maps/dist/angular-google-maps.js',
            'src/main/webapp/bower_components/angular-datatables/dist/angular-datatables.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/bootstrap/angular-datatables.bootstrap.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/colreorder/angular-datatables.colreorder.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/columnfilter/angular-datatables.columnfilter.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/light-columnfilter/angular-datatables.light-columnfilter.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/colvis/angular-datatables.colvis.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/fixedcolumns/angular-datatables.fixedcolumns.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/fixedheader/angular-datatables.fixedheader.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/scroller/angular-datatables.scroller.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/tabletools/angular-datatables.tabletools.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/buttons/angular-datatables.buttons.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/select/angular-datatables.select.js',
            // endbower
            'src/main/webapp/app/app.module.js',
            'src/main/webapp/app/app.state.js',
            'src/main/webapp/app/app.constants.js',
            'src/main/webapp/app/**/*.+(js|html)',
            'src/test/javascript/spec/helpers/module.js',
            'src/test/javascript/spec/helpers/httpBackend.js',
            'src/test/javascript/**/!(karma.conf|protractor.conf).js'
        ],


        // list of files / patterns to exclude
        exclude: ['src/test/javascript/e2e/**'],

        preprocessors: {
            './**/*.js': ['coverage']
        },

        reporters: ['dots', 'jenkins', 'coverage', 'progress'],

        jenkinsReporter: {
            
            outputFile: 'target/test-results/karma/TESTS-results.xml'
        },

        coverageReporter: {
            
            dir: 'target/test-results/coverage',
            reporters: [
                {type: 'lcov', subdir: 'report-lcov'}
            ]
        },

        // web server port
        port: 9876,

        // level of logging
        // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
        logLevel: config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: false,

        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers: ['PhantomJS'],

        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: false,

        // to avoid DISCONNECTED messages when connecting to slow virtual machines
        browserDisconnectTimeout : 10000, // default 2000
        browserDisconnectTolerance : 1, // default 0
        browserNoActivityTimeout : 4*60*1000 //default 10000
    });
};
