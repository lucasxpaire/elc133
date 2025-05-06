module.exports = {
  async rewrites() {
    return [
      {
        source: '/api/plantas/:path*',
        destination: process.env.NEXT_PUBLIC_API_URL + '/plantas/:path*', // Usando variável de ambiente
      },
      {
        source: '/api/login',
        destination: process.env.NEXT_PUBLIC_API_URL + '/login',
      },
    ];
  },
};
