export interface QueryResponseJson {
  idQuery: number;
  usuario: string;
  pilasResult?: PilaCoinJson[];
  blocosResult?: BlocoJson[];
  usuariosResult?: any[];
}

export interface BlocoJson {
  numeroBloco: number;
  nonceBlocoAnterior: string;
  nonce: string;
  chaveUsuarioMinerador: Uint8Array;
  nomeUsuarioMinerador: string;
  transacoes: TransacaoJson[];
  minerado: boolean;
}

export interface TransacaoJson {
  id: string;
  chaveUsuarioOrigem: Uint8Array;
  chaveUsuarioDestino: Uint8Array;
  assinatura: Uint8Array;
  origem: string;
  noncePila: string;
  status: string;
  dataTransacao: Date;
}

export interface PilaCoinJson {
  id: number;
  dataCriacao: Date;
  chaveCriador: Uint8Array;
  nomeCriador: string;
  status: string; // Assuming PilaCoin.Status is a string enum
  nonce: string;
  transacoes: TransacaoJson[];
}