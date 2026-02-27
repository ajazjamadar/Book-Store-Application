# Build context is the project root (set in docker-compose.yml).
# Static files live in frontend/public; nginx config in docker/nginx/.
FROM nginx:1.27-alpine

# Remove the default nginx config
RUN rm /etc/nginx/conf.d/default.conf

# Inject our nginx config (API proxy + static file serving)
COPY docker/nginx/nginx.conf /etc/nginx/conf.d/default.conf

# Copy static frontend assets
COPY frontend/public /usr/share/nginx/html

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
    CMD wget -qO- http://localhost/ > /dev/null || exit 1
