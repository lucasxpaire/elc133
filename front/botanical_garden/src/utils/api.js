// utils/api.js

const API_URL = 'https://elc133-production.up.railway.app/plantas';

// Função para buscar todas as plantas
export const getPlantas = async () => {
  const response = await fetch(API_URL);
  if (!response.ok) {
    throw new Error('Erro ao buscar plantas');
  }
  return response.json();
};
