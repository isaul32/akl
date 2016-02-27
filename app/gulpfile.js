/* jshint camelcase: false */
'use strict';

var gulp = require('gulp'),
    gutil = require('gulp-util'),
    autoprefixer = require('gulp-autoprefixer'),
    uglify = require('gulp-uglify'),
    concat = require('gulp-concat'),
    sass = require('gulp-sass'),
    rename = require('gulp-rename'),
    cssnano = require('gulp-cssnano'),
    ngAnnotate = require('gulp-ng-annotate'),
    jshint = require('gulp-jshint'),
    browserify = require('browserify'),
    watchify = require('watchify'),
    source = require('vinyl-source-stream'),
    buffer = require('vinyl-buffer'),
    flatten = require('gulp-flatten'),
    sequence = require('gulp-sequence'),
    del = require('del'),
    url = require('url'),
    fs = require('fs'),
    http = require('http'),
    httpProxy = require('http-proxy'),
    express = require('express');

var config = {
    app: 'src/main/javascript/',
    dist: 'target/dist/',
    scss: 'src/main/scss/',
    ckeditor: 'src/main/ckeditor/',
    port: 9000,
    apiPort: 8080
};

var bopts = {
    entries: [config.app + 'main.js'],
    cache: {},
    packageCache: {}
    //plugin: [watchify] // Watch
};

var b = browserify(bopts);

var bundle = function () {
    b.bundle()
        .on('error', gutil.log.bind(gutil, 'Browserify error'))
        .pipe(source('bundle.js'))
        .pipe(ngAnnotate())
        //.pipe(buffer())
        //.pipe(uglify())
        .pipe(gulp.dest(config.dist + 'js'));
};

gulp.task('clean', function () {
    return del(config.dist);
});

gulp.task('views', function() {
    return gulp.src([config.app + '**/*.html', '!' + config.dist + '**/*.html'])
        .pipe(gulp.dest(config.dist));
});

gulp.task('assets', ['fonts', 'images', 'sass', 'ckeditor', 'i18n'], function () {
    return gulp.src([
        config.app + 'favicon.ico',
        config.app + 'robots.txt'
    ])
        .pipe(gulp.dest(config.dist));
});

gulp.task('fonts', function () {
    return gulp.src(['node_modules/bootstrap-sass/assets/fonts/**/*',
        'node_modules/font-awesome/fonts/**/*'])
        .pipe(gulp.dest(config.dist + 'fonts'));
});

gulp.task('images', function () {
    return gulp.src([config.app + 'assets/images/**/*'])
        .pipe(gulp.dest(config.dist + 'images'));
});

gulp.task('sass', function () {
    return gulp.src(config.scss + 'main.scss')
        .pipe(sass().on('error', sass.logError))
        .pipe(gulp.dest(config.dist + 'css'));
});

gulp.task('ckeditor', function () {
    return gulp.src(['node_modules/ckeditor/**/*',
            config.ckeditor + '**/*'])
        .pipe(gulp.dest(config.dist + 'ckeditor'));
});

gulp.task('i18n', function () {
    return gulp.src([config.app + 'i18n/**/*',
            'node_modules/angular-i18n/angular-locale_fi.js',
            'node_modules/angular-i18n/angular-locale_en.js'])
        .pipe(gulp.dest(config.dist + 'i18n'));
});

gulp.task('browserify', function () {
    return bundle();
});

gulp.task('dev', function () {
    var proxy = httpProxy.createProxyServer({
        target: {
            host: 'localhost',
            port: 8080,
            ws: true
        }
    });

    proxy.on('error', function (err, req, res) {
        // Try reconnect
        console.log('Proxy cannot connect to API. Please start API service.');
        proxy.close();
    });

    // Websocket events
    proxy.on('open', function (proxySocket) {
        console.log('WS Client connected');

        proxySocket.on('data', function (msg) {
            console.log(msg);
        });
    });
    proxy.on('close', function (res, socket, head) {
        console.log('WS Client disconnected');
    });

    // App server
    var app = express();

    app.use(express.static(config.dist));

    app.all(/^\/api/, function(req, res) {
        proxy.web(req, res);
    });

    app.all(/^\/health/, function(req, res) {
        proxy.web(req, res);
    });

    app.all(/^\/configprops/, function(req, res) {
        proxy.web(req, res);
    });

    app.all(/^\/v2\/api-docs/, function(req, res) {
        proxy.web(req, res);
    });

    app.all(/^\/swagger-ui/, function(req, res) {
        // /swagger-ui -> /
        req.url =
            '/' + req.url
                .split("/")
                .slice(2, req.url.split("/").length)
                .join("/");

        proxy.web(req, res);
    });

    app.all(/^\/metrics/, function(req, res) {
        proxy.web(req, res);
    });
    app.get(/^\/websocket/, function(req, res) {
        proxy.web(req, res);
    });

    app.all(/^\/dump/, function(req, res) {
        proxy.web(req, res);
    });

    app.all(/^\/console/, function(req, res) {
        proxy.web(req, res);
    });

    http.createServer(app).listen(config.port)
        .on('upgrade', function (req, socket, head) {
            proxy.ws(req, socket, head);
        });
});

gulp.task('watch', function() {
    gulp.watch(config.app + '**/*.js', ['browserify']);
    gulp.watch(config.scss + '**/*.scss', ['sass']);
    gulp.watch([config.app + '**/*.html', '!' + config.dist + '**/*.html'], ['views']);
});

gulp.task('build', sequence('clean', ['views', 'assets', 'browserify']));
gulp.task('dist', ['build']);
gulp.task('serve', sequence('build', ['dev', 'watch']));
gulp.task('default', ['build']);

b.on('update', bundle);
b.on('log', gutil.log);
