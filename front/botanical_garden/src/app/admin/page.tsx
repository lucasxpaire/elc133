'use client';
import { useState } from 'react';
import { useRouter } from 'next/navigation';

export default function AdminPage() {
  const [autenticado, setAutenticado] = useState(false);
  const [senhaInput, setSenhaInput] = useState('');
  const [usernameInput, setUsernameInput] = useState('');

  const [nomePopular, setNomePopular] = useState('');
  const [nomeCientifico, setNomeCientifico] = useState('');
  const [descricao, setDescricao] = useState('');
  const [cuidados, setCuidados] = useState('');
  const [imagemUrl, setImagemUrl] = useState('');
  const [alturaMaxima, setAlturaMaxima] = useState('');
  const [corDaFlor, setCorDaFlor] = useState('');
  const [origem, setOrigem] = useState('');
  const [tipoDeFolha, setTipoDeFolha] = useState('');

  const router = useRouter(); // Hook do Next.js para navegação

  const handleAuth = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      // Faz a requisição para autenticação com nome de usuário e senha
      const loginResponse = await fetch('/api/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username: usernameInput, password: senhaInput }),
      });

      if (!loginResponse.ok) {
        const errorData = await loginResponse.json().catch(() => ({}));
        alert(errorData.message || 'Falha na autenticação!');
        router.push("/")
        return;
      }

      setAutenticado(true);
    } catch (error) {
      console.error('Erro no login:', error);
      alert('Erro ao tentar fazer login!');
    }
  };

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    const newPlanta = {
      nomePopular,
      nomeCientifico,
      descricao,
      cuidados,
      imagemUrl,
      alturaMaxima,
      corDaFlor,
      origem,
      tipoDeFolha,
    };

    await fetch('/api/plantas', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newPlanta),
    });

    alert('🌱 Planta cadastrada com sucesso!');
    setNomePopular('');
    setNomeCientifico('');
    setDescricao('');
    setCuidados('');
    setImagemUrl('');
    setAlturaMaxima('');
    setCorDaFlor('');
    setOrigem('');
    setTipoDeFolha('');
  };

  const handleVoltar = () => {
    router.push('/'); // Navega para a página inicial
  };

  if (!autenticado) {
    return (
      <main className="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-50 to-white px-4">
        <div className="bg-white p-8 rounded-xl shadow-md w-full max-w-md">
          <h2 className="text-2xl font-bold mb-6 text-green-800 text-center">
            Acesso Administrativo
          </h2>
          <form onSubmit={handleAuth} className="flex flex-col gap-4">
            <input
              type="text"
              placeholder="Digite o nome de usuário"
              value={usernameInput}
              onChange={(e) => setUsernameInput(e.target.value)}
              className="border p-2 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-green-500"
            />
            <input
              type="password"
              placeholder="Digite a senha"
              value={senhaInput}
              onChange={(e) => setSenhaInput(e.target.value)}
              className="border p-2 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-green-500"
            />
            <button
              type="submit"
              className="bg-green-700 text-white py-2 rounded hover:bg-green-800 transition"
            >
              Entrar
            </button>
          </form>
        </div>
      </main>
    );
  }

  return (
    <main className="min-h-screen bg-gradient-to-b from-green-50 to-white py-10 px-4 flex justify-center">
      <div className="bg-white rounded-3xl shadow-xl p-8 w-full max-w-2xl">
        <button
          onClick={handleVoltar}
          className="text-sm text-green-600 mb-4 flex items-center gap-2 hover:underline"
        >
          <span>←</span> Voltar para a Home
        </button>

        <h2 className="text-3xl font-bold text-green-900 mb-6 text-center font-serif">
          🌱 Cadastro de Nova Planta
        </h2>

        <form onSubmit={handleSubmit} className="space-y-5">
          {[ 
            { label: 'Nome Popular', value: nomePopular, setter: setNomePopular },
            { label: 'Nome Científico', value: nomeCientifico, setter: setNomeCientifico },
            { label: 'Descrição', value: descricao, setter: setDescricao, type: 'textarea' },
            { label: 'Cuidados', value: cuidados, setter: setCuidados },
            { label: 'URL da Imagem (Usar o link de alguma imagem disponível na web)', value: imagemUrl, setter: setImagemUrl },
            { label: 'Altura Máxima', value: alturaMaxima, setter: setAlturaMaxima },
            { label: 'Cor da Flor', value: corDaFlor, setter: setCorDaFlor },
            { label: 'Origem', value: origem, setter: setOrigem },
            { label: 'Tipo de Folha', value: tipoDeFolha, setter: setTipoDeFolha },
          ].map((field, index) => (
            <div key={index} className="flex flex-col">
              <label className="text-sm font-medium text-green-800 mb-1">
                {field.label}
              </label>
              {field.type === 'textarea' ? (
                <textarea
                  value={field.value}
                  onChange={(e) => field.setter(e.target.value)}
                  className="border p-2 rounded-md shadow-sm resize-none h-24 focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              ) : (
                <input
                  type="text"
                  value={field.value}
                  onChange={(e) => field.setter(e.target.value)}
                  className="border p-2 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              )}
            </div>
          ))}

          <div className="pt-4">
            <button
              type="submit"
              className="w-full bg-green-700 text-white py-3 rounded-lg hover:bg-green-800 font-medium transition shadow-md"
            >
              Cadastrar Planta
            </button>
          </div>
        </form>
      </div>
    </main>
  );
}
