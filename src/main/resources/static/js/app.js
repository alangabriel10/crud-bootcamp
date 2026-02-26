// URL base da API
const API_URL = 'http://localhost:8080/api/usuarios';

let usuarioIdParaExcluir = null;
let modalUsuario, modalExcluir;

// Inicialização quando a página carregar
document.addEventListener('DOMContentLoaded', function() {
    // Inicializar modais do Bootstrap
    modalUsuario = new bootstrap.Modal(document.getElementById('modalUsuario'));
    modalExcluir = new bootstrap.Modal(document.getElementById('modalExcluir'));

    // Carregar usuários
    carregarUsuarios();

    // Adicionar validação ao formulário
    configurarValidacaoFormulario();
});


async function carregarUsuarios() {
    try {
        const response = await fetch(API_URL);

        if (!response.ok) {
            throw new Error('Erro ao carregar usuários');
        }

        const usuarios = await response.json();
        exibirUsuarios(usuarios);
        atualizarContador(usuarios.length);

    } catch (error) {
        console.error('Erro:', error);
        mostrarAlerta('Erro ao carregar usuários!', 'danger');
        document.getElementById('tabelaUsuarios').innerHTML = `
            <tr>
                <td colspan="4" class="text-center text-danger py-4">
                    <i class="bi bi-exclamation-triangle fs-1"></i>
                    <p class="mt-2">Erro ao carregar usuários. Verifique se o servidor está rodando.</p>
                </td>
            </tr>
        `;
    }
}


function exibirUsuarios(usuarios) {
    const tbody = document.getElementById('tabelaUsuarios');

    if (usuarios.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="4" class="text-center py-5">
                    <i class="bi bi-inbox fs-1 text-muted"></i>
                    <p class="mt-3 text-muted">Nenhum usuário cadastrado</p>
                    <button class="btn btn-primary mt-2" onclick="abrirModalCriar()">
                        <i class="bi bi-plus-circle"></i> Cadastrar Primeiro Usuário
                    </button>
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = usuarios.map(usuario => `
        <tr class="fade-in">
            <td class="align-middle fw-bold">${usuario.id}</td>
            <td class="align-middle">${usuario.nome}</td>
            <td class="align-middle">${usuario.email}</td>
            <td class="align-middle text-center">
                <button class="btn btn-sm btn-info btn-action me-1" 
                        onclick="abrirModalEditar(${usuario.id})" 
                        title="Editar">
                    <i class="bi bi-pencil"></i>
                </button>
                <button class="btn btn-sm btn-danger btn-action" 
                        onclick="abrirModalExcluir(${usuario.id}, '${usuario.nome}')" 
                        title="Excluir">
                    <i class="bi bi-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');
}



function abrirModalCriar() {
    document.getElementById('modalTitulo').innerHTML = `
        <i class="bi bi-person-plus"></i> Novo Usuário
    `;
    document.getElementById('usuarioId').value = '';
    document.getElementById('usuarioNome').value = '';
    document.getElementById('usuarioEmail').value = '';

    // Remover classes de validação
    document.getElementById('formUsuario').classList.remove('was-validated');

    modalUsuario.show();
}



async function abrirModalEditar(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);

        if (!response.ok) {
            throw new Error('Usuário não encontrado');
        }

        const usuario = await response.json();

        document.getElementById('modalTitulo').innerHTML = `
            <i class="bi bi-pencil"></i> Editar Usuário
        `;
        document.getElementById('usuarioId').value = usuario.id;
        document.getElementById('usuarioNome').value = usuario.nome;
        document.getElementById('usuarioEmail').value = usuario.email;

        // Remover classes de validação
        document.getElementById('formUsuario').classList.remove('was-validated');

        modalUsuario.show();

    } catch (error) {
        console.error('Erro:', error);
        mostrarAlerta('Erro ao carregar dados do usuário!', 'danger');
    }
}



async function salvarUsuario() {
    const form = document.getElementById('formUsuario');

    // Validar formulário
    if (!form.checkValidity()) {
        form.classList.add('was-validated');
        return;
    }

    const id = document.getElementById('usuarioId').value;
    const nome = document.getElementById('usuarioNome').value;
    const email = document.getElementById('usuarioEmail').value;

    const usuario = { nome, email };

    try {
        let response;

        if (id) {
            // Atualizar usuário existente
            response = await fetch(`${API_URL}/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(usuario)
            });
        } else {
            // Criar novo usuário
            response = await fetch(API_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(usuario)
            });
        }

        if (!response.ok) {
            const erro = await response.text();
            throw new Error(erro);
        }

        modalUsuario.hide();
        carregarUsuarios();

        const mensagem = id ? 'Usuário atualizado com sucesso!' : 'Usuário criado com sucesso!';
        mostrarAlerta(mensagem, 'success');

    } catch (error) {
        console.error('Erro:', error);
        mostrarAlerta(error.message || 'Erro ao salvar usuário!', 'danger');
    }
}



function abrirModalExcluir(id, nome) {
    usuarioIdParaExcluir = id;
    document.getElementById('usuarioExcluirNome').textContent = nome;
    modalExcluir.show();
}



async function confirmarExclusao() {
    try {
        const response = await fetch(`${API_URL}/${usuarioIdParaExcluir}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Erro ao excluir usuário');
        }

        modalExcluir.hide();
        carregarUsuarios();
        mostrarAlerta('Usuário excluído com sucesso!', 'success');

    } catch (error) {
        console.error('Erro:', error);
        mostrarAlerta('Erro ao excluir usuário!', 'danger');
    }
}



function mostrarAlerta(mensagem, tipo) {
    const container = document.getElementById('alertContainer');
    const alerta = document.createElement('div');

    alerta.className = `alert alert-${tipo} alert-dismissible fade show`;
    alerta.innerHTML = `
        <i class="bi bi-${tipo === 'success' ? 'check-circle' : 'exclamation-triangle'}"></i>
        ${mensagem}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    container.appendChild(alerta);

    // Remover alerta após 5 segundos
    setTimeout(() => {
        alerta.remove();
    }, 5000);
}



function atualizarContador(total) {
    const badge = document.querySelector('#totalUsuarios .badge');
    badge.textContent = total;
}



function configurarValidacaoFormulario() {
    const form = document.getElementById('formUsuario');

    form.addEventListener('submit', function(event) {
        event.preventDefault();
        event.stopPropagation();
        salvarUsuario();
    });

    // Permitir submit com Enter
    document.getElementById('usuarioEmail').addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            salvarUsuario();
        }
    });
}