// next.config.js
module.exports = {
  async rewrites() {
    return [
      {
        source: '/api/plantas/:path*',
        destination: 'https://elc133-production.up.railway.app/plantas/:path*',
      },
      {
        source: '/api/login', // Novo caminho local
        destination: 'https://elc133-production.up.railway.app/login', // Redireciona para o login da API externa
      },
    ];
  },
};
