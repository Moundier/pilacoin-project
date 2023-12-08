
export type TransacaoJson = {
    id: number;
    chaveUsuarioOrigem: string;
    chaveUsuarioDestino: string;
    assinatura: string;
    origem: string;
    noncePila: string;
    status: string;
    dataTransacao: string;
};

export type PilaCoinJson = {
    id: number;
    dataCriacao: string;
    chaveCriador: string;
    nomeCriador: string;
    status: string;
    nonce: string;
    transacoes: TransacaoJson[];
    selected: boolean;
};

export type BlocoJson = {
    numeroBloco: number;
    nonceBlocoAnterior: string;
    nonce: string;
    chaveUsuarioMinerador: string;
    nomeUsuarioMinerador: string;
    transacoes: TransacaoJson[];
    minerado: boolean;
};

export type UsuarioJson = {
    id: number;
    nome: string;
    chavePublica: string;
    selected: boolean;
    isButtonClicked?: boolean; // New property to track button click
};

export type QueryResponseJson = {
    idQuery: number;
    usuario: string;
    pilasResult: PilaCoinJson[];
    blocosResult: BlocoJson[];
    usuariosResult: UsuarioJson[];
};

export type QueryResponse<T> = {
    idQuery: number;
    usuario: string;
    result: T[];
};

export interface SseMessage {
    timestamp?: number;
    className: string;
    message: string;
    messageType: SseMessageType;
}

export enum SseMessageType {
    MINED_BLOCK = 'MINED_BLOCK',
    VALID_BLOCK = 'VALID_BLOCK',
    MINED_PILA = 'MINED_PILA',
    VALID_PILA = 'VALID_PILA',
    TRANSFERRED_PILA = 'TRANSFERRED_PILA',
}

export interface Transferencia {
    id: number;
    status: string;
    chaveUsuarioOrigem: Uint8Array;
    chaveUsuarioDestino: Uint8Array; // get and send
    nomeUsuarioOrigem: string;
    nomeUsuarioDestino: string; // get and send
    noncePila: string; // get and send
    assinatura: Uint8Array;
    dataTransacao: Date;
}

export interface PilaCoin {
    id: number;
    dataCriacao: Date;
    chaveCriador: Uint8Array;
    nomeCriador: string;
    status: Status;
    nonce: string;
}

export enum Status {
    AG_VALIDACAO = "AG_VALIDACAO",
    AG_BLOCO = "AG_BLOCO",
    BLOCO_EM_VALIDACAO = "BLOCO_EM_VALIDACAO",
    VALIDO = "VALIDO",
    INVALIDO = "INVALIDO",
}