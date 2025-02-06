INSERT INTO tb_user (email, nome, role, senha,status_usuario,cnpj_banco)
SELECT 'root@gmail.com', 'root', 'ADMIN', '$2a$12$W4ga0wBDtSMXGY0y41/PAuY8o0E.SGucCpddVRY6gvZhPxA16bpGi', 'ATIVO','20555110000127'
WHERE NOT EXISTS (SELECT email FROM tb_user WHERE id = 1);
