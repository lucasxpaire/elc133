'use client';
import Link from 'next/link';
import { useEffect, useState } from 'react';

type Planta = {
  _id: string;
  nomePopular: string;
  nomeCientifico: string;
  descricao: string;
  cuidados: string;
  imagemUrl: string;
  alturaMaxima?: string;
  corDaFlor?: string;
  origem?: string;
  tipoDeFolha?: string;
};

export default function Home() {
  const [plantas, setPlantas] = useState<Planta[]>([]);
  const [filtro, setFiltro] = useState('');
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const fetchPlantas = async () => {
      try {
        const res = await fetch('/api/plantas');
        const data = await res.json();
        setPlantas(data);
      } catch (error) {
        console.error("Erro ao carregar as plantas:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchPlantas();
  }, []);

  const plantasFiltradas = plantas.filter((planta) =>
    planta.nomePopular.toLowerCase().includes(filtro.toLowerCase())
  );

  if (loading) {
    return (
      <div className="flex flex-col justify-center items-center h-screen text-green-800 bg-gradient-to-br from-green-50 to-white">
        <div className="animate-spin w-12 h-12 border-[5px] border-green-600 border-t-transparent rounded-full mb-4" />
        <p className="text-lg font-medium">Carregando plantas...</p>
      </div>
    );
  }

  return (
    <main className="p-6 md:p-10 max-w-7xl mx-auto bg-gradient-to-b from-[#f3fef4] to-white min-h-screen">
      <header className="flex flex-col items-center justify-center mb-12">
        <h1 className="text-5xl font-extrabold text-center text-green-900 tracking-tight font-serif mb-4">
          🌿 Jardim Botânico Virtual
        </h1>
        <p className="text-gray-600 text-center max-w-2xl">
          Explore uma coleção de plantas com informações botânicas e dicas de cuidados.
        </p>
      </header>

      <div className="flex flex-col md:flex-row justify-between items-center gap-4 mb-10">
        <input
          type="text"
          placeholder="Buscar por nome popular..."
          value={filtro}
          onChange={(e) => setFiltro(e.target.value)}
          className="w-full md:w-96 px-4 py-2 border border-green-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-green-500"
        />
        <Link
          href="/admin"
          className="bg-green-700 text-white px-5 py-2 rounded-lg hover:bg-green-800 shadow-md transition"
        >
          + Nova Planta
        </Link>
      </div>

      <div className="grid gap-12 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3">
        {plantasFiltradas.length > 0 ? (
          plantasFiltradas.map((planta) => (
            <Link
              key={planta._id}
              href={`/planta/${planta._id}`}
              className="relative bg-white/80 backdrop-blur-lg rounded-3xl overflow-hidden shadow-lg hover:shadow-2xl border border-green-100 transition-transform hover:-translate-y-1 group"
            >
              <img
                src={planta.imagemUrl}
                alt={planta.nomePopular}
                className="w-full h-52 object-cover rounded-t-3xl transition-opacity duration-300 group-hover:opacity-90"
                onError={(e) => (e.currentTarget.src = '/placeholder.png')}
              />
              <div className="p-6">
                <h2 className="text-2xl font-semibold text-green-800 mb-1 group-hover:underline">
                  {planta.nomePopular}
                </h2>
                <p className="text-sm text-gray-500 italic mb-3">{planta.nomeCientifico}</p>
                <p className="text-gray-700 text-sm mb-4 line-clamp-3">{planta.descricao}</p>
                <ul className="text-sm text-gray-600 space-y-1">
                  <li><span className="font-medium text-green-900">🌿 Cuidados:</span> {planta.cuidados}</li>
                  {planta.alturaMaxima && <li><span className="font-medium text-green-900">📏 Altura:</span> {planta.alturaMaxima}</li>}
                  {planta.corDaFlor && <li><span className="font-medium text-green-900">🌸 Flor:</span> {planta.corDaFlor}</li>}
                  {planta.origem && <li><span className="font-medium text-green-900">🌍 Origem:</span> {planta.origem}</li>}
                  {planta.tipoDeFolha && <li><span className="font-medium text-green-900">🍃 Folha:</span> {planta.tipoDeFolha}</li>}
                </ul>
              </div>
            </Link>
          ))
        ) : (
          <p className="text-center text-gray-600 text-lg col-span-full">Nenhuma planta encontrada.</p>
        )}
      </div>

      <footer className="text-center text-sm text-gray-500 py-10 border-t mt-16 border-green-100">
  Desenvolvido por{' '}
  <a className="text-green-800 font-semibold" href="https://github.com/lucasxpaire" target="_blank" rel="noopener noreferrer">
    Lucas Paire
  </a>{' '}
  |{' '}
  <a className="text-green-800 font-semibold" href="https://github.com/thurarbogacki" target="_blank" rel="noopener noreferrer">
    Arthur Bogacki
  </a>{' '}
  |{' '}
  <a className="text-green-800 font-semibold" href="https://github.com/leandrogalbarino" target="_blank" rel="noopener noreferrer">
    Leandro Oliveira
  </a>{' '}
  · Projeto ELC133 · 2025
</footer>

    </main>
  );
}
