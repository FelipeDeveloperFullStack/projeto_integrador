package br.com.pitdog.service.conf;

import java.util.List;

import br.com.pitdog.model.conf.ConfiguracaoBackup;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;

public class ConfiguracaoBackupService extends GenericDaoImpl<ConfiguracaoBackup, Long>{

	private static final long serialVersionUID = 4360526737828013305L;
	
	public void salvar(ConfiguracaoBackup configuracaoBackup){
		List<ConfiguracaoBackup> configuracaoBackups = super.findAll();
		if(configuracaoBackups.isEmpty()){
			super.save(configuracaoBackup);
		}else{
			if(configuracaoBackups.size() == 1){
				for(ConfiguracaoBackup c : configuracaoBackups){
					c.setAutomatico(configuracaoBackup.isAutomatico());
					c.setCaminhoArquivoMysqlDump(configuracaoBackup.getCaminhoArquivoMysqlDump());
					c.setDiretorio(configuracaoBackup.getDiretorio());
					c.setEmail(configuracaoBackup.getEmail());
					c.setResponsavel(configuracaoBackup.getResponsavel());
					c.setTempoBackupAutomatico(configuracaoBackup.getTempoBackupAutomatico());
					super.save(c);
				}
			}
		}
	}

}
