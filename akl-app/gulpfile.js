'use strict';

var gulp = require('gulp'),
    autoprefixer = require('gulp-autoprefixer'),
    templatecache = require('gulp-angular-templatecache'),
    concat = require('gulp-concat'),
    sass = require('gulp-sass'),
    rename = require('gulp-rename'),
    cssnano = require('gulp-cssnano'),
    annotate = require('gulp-ng-annotate'),
    flatten = require('gulp-flatten'),
    sequence = require('gulp-sequence'),
    uglify = require('gulp-uglify'),
    gulpif = require('gulp-if'),
    streamify = require('gulp-streamify'),
    ts = require('gulp-typescript'),
    source = require('vinyl-source-stream'),
    browserify = require('browserify'),
    del = require('del'),
    url = require('url'),
    fs = require('fs'),
    http = require('http'),
    httpProxy = require('http-proxy'),
    express = require('express');

var config = {
    "app": "src/main/app/",
    "dist": "target/dist/",
    "assets": "src/main/assets/",
    "i18n": "src/main/i18n/",
    "scss": "src/main/scss/",
    "ckeditor": "src/main/ckeditor/",
    "port": 9000,
    "apiPort": 8080
};

var project = ts.createProject('tsconfig.json');

var production = false;

var b = browserify({
    entries: 'dependencies.js',
    debug: !production
});

// Tasks
gulp.task('clean', function () {
    return del(config.dist);
});

gulp.task('templates', function() {
    return gulp.src([config.app + '**/*.html', '!' + config.app + 'index.html'])
        .pipe(templatecache('templates.js', { module: 'templateCache', standalone: true }))
        .pipe(gulp.dest(config.dist + 'js'));
});

gulp.task('views', function() {
    return gulp.src([config.app + 'index.html'])
        .pipe(gulp.dest(config.dist));
});

gulp.task('assets', ['fonts', 'images', 'sass', 'ckeditor', 'i18n'], function () {
    return gulp.src([
        config.app + 'favicon.ico',
        config.app + 'robots.txt',
        config.app + 'sitemap.xml'
    ])
        .pipe(gulp.dest(config.dist));
});

gulp.task('fonts', function () {
    return gulp.src(['node_modules/bootstrap-sass/assets/fonts/**/*',
        'node_modules/font-awesome/fonts/**/*', config.assets + 'fonts/**/*'])
        .pipe(gulp.dest(config.dist + 'fonts'));
});

gulp.task('images', function () {
    return gulp.src([config.assets + 'images/**/*'])
        .pipe(gulp.dest(config.dist + 'images'));
});

gulp.task('sass', function () {
    return gulp.src(config.scss + 'main.scss')
        .pipe(sass().on('error', sass.logError))
        .pipe(gulp.dest(config.dist + 'css'))
});

gulp.task('ckeditor', function () {
    return gulp.src(['node_modules/ckeditor/**/*', config.ckeditor + '**/*'])
        .pipe(gulp.dest(config.dist + 'ckeditor'));
});

gulp.task('i18n', function () {
    return gulp.src([config.i18n + '**/*.json',
            'node_modules/angular-i18n/angular-locale_fi.js',
            'node_modules/angular-i18n/angular-locale_en.js'])
        .pipe(gulp.dest(config.dist + 'i18n'))
});

gulp.task('dependencies', function () {
    return b.bundle()
        .pipe(source('dependencies.js'))
        .pipe(gulpif(production, streamify(uglify())))
        .pipe(gulp.dest(config.dist + 'js'));
});

gulp.task('compile', function () {
    return project.src()
        .pipe(ts(project))
        .pipe(annotate())
        .pipe(gulpif(production, streamify(uglify())))
        .pipe(concat('bundle.js'))
        .pipe(gulp.dest(config.dist + 'js'));
});

gulp.task('dev', function () {
    var proxy = httpProxy.createProxyServer({
        target: {
            host: 'localhost',
            port: config.apiPort,
            ws: true
        }
    });

    // Try reconnect
    proxy.on('error', function () {
        console.log('Proxy cannot connect to API. Please start API service.');
        proxy.close();
    });

    // Websocket events
    proxy.on('open', function (proxySocket) {
        console.log('WS Client connected');

        proxySocket.on('data', function (msg) {
            //console.log(msg);
        });
    });
    proxy.on('close', function () {
        console.log('WS Client disconnected');
    });

    // App server
    var app = express();

    app.use('/', express.static(config.dist));

    app.all(/^\/akl-service/, function(req, res) {
        proxy.web(req, res);
    });

    http.createServer(app).listen(config.port).on('upgrade', function (req, socket, head) {
        proxy.ws(req, socket, head);
    });
});

gulp.task('watch', function() {
    gulp.watch('dependencies.js', ['dependencies']);
    gulp.watch(config.app + '**/*.ts', ['compile']);
    gulp.watch(config.i18n + '**/*.json', ['i18n']);
    gulp.watch(config.scss + '**/*.scss', ['sass']);
    gulp.watch([config.app + '**/*.html', '!' + config.dist + '**/*.html'], ['templates']);
});

gulp.task('build', sequence('clean', ['dependencies', 'compile', 'templates', 'assets', 'views']));

gulp.task('prod', function () {
    production = true;
});

gulp.task('dist', sequence('prod', 'build'));

gulp.task('serve', sequence('build', ['dev', 'watch']));

gulp.task('default', ['build']);
