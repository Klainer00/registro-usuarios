package com.perfumeria.registro_usuario.model;

public enum EnumRol {
    CLIENTE(1),
    VENDEDOR(2),
    GERENTE(3),
    ADMINISTRADOR(4);

    private final int codigo;

    EnumRol(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}