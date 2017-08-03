const
    gulp = require('gulp'),
    templatecache = require('gulp-angular-templatecache'),
    concat = require('gulp-concat'),
    sass = require('gulp-sass'),
    annotate = require('gulp-ng-annotate'),
    sequence = require('gulp-sequence'),
    uglify = require('gulp-uglify'),
    gulpif = require('gulp-if'),
    streamify = require('gulp-streamify'),
    ts = require('gulp-typescript'),
    typings = require('gulp-typings'),
    source = require('vinyl-source-stream'),
    browserify = require('browserify'),
    del = require('del'),
    http = require('http'),
    httpProxy = require('http-proxy'),
    express = require('express');

const config = {
    app: 'src/main/app/',
    dist: 'target/dist/',
    assets: 'src/main/assets/',
    i18n: 'src/main/i18n/',
    scss: 'src/main/scss/',
    ckeditor: 'src/main/ckeditor/',
    port: 9000,
    apihost: 'localhost',
    apiPort: 8080
};

const project = ts.createProject('tsconfig.json');

let production = false;

const b = browserify({
    entries: 'dependencies.js',
    debug: !production
});

gulp.task('typings', () => {
    return gulp.src('./typings.json')
        .pipe(typings());
});

gulp.task('clean', () => {
    return del(config.dist);
});

gulp.task('templates', () => {
    return gulp.src([config.app + '**/*.html', '!' + config.app + 'index.html'])
        .pipe(templatecache('templates.js', { module: 'templateCache', standalone: true }))
        .pipe(gulp.dest(config.dist + 'js'));
});

gulp.task('views', () => {
    return gulp.src([config.app + 'index.html'])
        .pipe(gulp.dest(config.dist));
});

gulp.task('assets', ['fonts', 'images', 'sass', 'ckeditor', 'i18n'], () => {
    return gulp.src([
        config.app + 'favicon.ico',
        config.app + 'robots.txt',
        config.app + 'sitemap.xml',
        config.app + 'manifest.json',
        config.app + 'sw.js'
    ])
        .pipe(gulp.dest(config.dist));
});

gulp.task('fonts', () => {
    return gulp.src(['node_modules/bootstrap-sass/assets/fonts/**/*',
        'node_modules/font-awesome/fonts/**/*', config.assets + 'fonts/**/*'])
        .pipe(gulp.dest(config.dist + 'fonts'));
});

gulp.task('images', () => {
    return gulp.src([config.assets + 'images/**/*'])
        .pipe(gulp.dest(config.dist + 'images'));
});

gulp.task('sass', () => {
    return gulp.src(config.scss + 'main.scss')
        .pipe(sass().on('error', sass.logError))
        .pipe(gulp.dest(config.dist + 'css'))
});

gulp.task('ckeditor', () => {
    return gulp.src(['node_modules/ckeditor/**/*', config.ckeditor + '**/*'])
        .pipe(gulp.dest(config.dist + 'ckeditor'));
});

gulp.task('i18n', () => {
    return gulp.src([config.i18n + '**/*.json'])
        .pipe(gulp.dest(config.dist + 'i18n'))
});

gulp.task('dependencies', () => {
    return b.bundle()
        .pipe(source('dependencies.js'))
        .pipe(gulpif(production, streamify(uglify())))
        .pipe(gulp.dest(config.dist + 'js'));
});

gulp.task('compile', () => {
    return project.src()
        .pipe(project())
        .pipe(annotate())
        .pipe(gulpif(production, streamify(uglify())))
        .pipe(concat('bundle.js'))
        .pipe(gulp.dest(config.dist + 'js'));
});

gulp.task('dev', () => {
    const proxy = httpProxy.createProxyServer({
        target: {
            host: config.apiHost,
            port: config.apiPort,
            ws: true
        }
    });

    // Try reconnect
    proxy.on('error', () => {
        console.error('Proxy cannot connect to API. Please start API service.');
        proxy.close();
    });

    // Websocket events
    proxy.on('open', (proxySocket) => {
        console.log('WS Client connected');

        proxySocket.on('data', (msg) => {
            //console.log(msg);
        });
    });
    proxy.on('close', () => {
        console.log('WS Client disconnected');
    });

    // App server
    const app = express();

    app.use('/', express.static(config.dist));

    app.all(/^\/akl-service/, (req, res) => {
        proxy.web(req, res);
    });

    http.createServer(app).listen(config.port).on('upgrade', (req, socket, head) => {
        proxy.ws(req, socket, head);
    });
});

gulp.task('watch', () => {
    gulp.watch('dependencies.js', ['dependencies']);
    gulp.watch(config.app + '**/*.ts', ['compile']);
    gulp.watch(config.i18n + '**/*.json', ['i18n']);
    gulp.watch(config.scss + '**/*.scss', ['sass']);
    gulp.watch([config.app + '**/*.html', '!' + config.dist + '**/*.html'], ['templates']);
});

gulp.task('prod', () => {
    production = true;
});

gulp.task('build', sequence('typings', 'clean', ['dependencies', 'compile', 'templates', 'assets', 'views']));

gulp.task('dist', sequence('prod', 'build'));

gulp.task('serve', sequence('build', ['dev', 'watch']));

gulp.task('default', ['build']);
