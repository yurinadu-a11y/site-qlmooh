const campaigns = [
  { code: 'QLM-2026-081', name: 'Festival de Verão', client: 'Conecta Recife', period: '01 Ago — 30 Ago', periodType: 'past', status: 'ended', statusLabel: 'Encerrada', payment: 'paid', paymentLabel: 'Confirmado', video: 'approved', videoLabel: 'Aprovado', insertions: '432.000', value: 'R$ 8.550,00' },
  { code: 'QLM-2026-079', name: 'Marca em Movimento', client: 'Grupo Movimento', period: '15 Ago — 15 Set', periodType: 'current', status: 'active', statusLabel: 'Ativa', payment: 'paid', paymentLabel: 'Confirmado', video: 'approved', videoLabel: 'Aprovado', insertions: '259.200', value: 'R$ 5.742,60' },
  { code: 'QLM-2026-076', name: 'Conecta Recife', client: 'Prefeitura de Recife', period: '01 Set — 30 Set', periodType: 'future', status: 'review', statusLabel: 'Em revisão', payment: 'pending', paymentLabel: 'Pendente', video: 'review', videoLabel: 'Em análise', insertions: '129.600', value: 'R$ 1.200,00' },
  { code: 'QLM-2026-084', name: 'Semana da Inovação', client: 'Nexus Forge', period: '05 Set — 11 Set', periodType: 'future', status: 'paid', statusLabel: 'Pagamento confirmado', payment: 'paid', paymentLabel: 'Confirmado', video: 'approved', videoLabel: 'Aprovado', insertions: '30.240', value: 'R$ 688,38' },
  { code: 'QLM-2026-085', name: 'Festival Criativo', client: 'Agência Aurora', period: '12 Set — 26 Set', periodType: 'future', status: 'review', statusLabel: 'Em revisão', payment: 'pending', paymentLabel: 'Pendente', video: 'pending', videoLabel: 'Aguardando', insertions: '64.800', value: 'R$ 1.435,50' },
  { code: 'QLM-2026-072', name: 'Cidade em Movimento', client: 'Mobiliza Brasil', period: '10 Jul — 31 Jul', periodType: 'past', status: 'ended', statusLabel: 'Encerrada', payment: 'paid', paymentLabel: 'Confirmado', video: 'approved', videoLabel: 'Aprovado', insertions: '95.040', value: 'R$ 2.079,00' }
];

const rows = document.querySelector('#campaign-rows');
const emptyState = document.querySelector('#empty-state');
const searchInput = document.querySelector('#search-input');
const statusFilter = document.querySelector('#status-filter');
const periodFilter = document.querySelector('#period-filter');

const render = () => {
  const term = searchInput.value.trim().toLocaleLowerCase('pt-BR');
  const status = statusFilter.value;
  const period = periodFilter.value;
  const visible = campaigns.filter(campaign => {
    const matchesTerm = !term || `${campaign.name} ${campaign.client} ${campaign.code}`.toLocaleLowerCase('pt-BR').includes(term);
    const matchesStatus = status === 'all' || campaign.status === status || campaign.payment === status;
    const matchesPeriod = period === 'all' || campaign.periodType === period;
    return matchesTerm && matchesStatus && matchesPeriod;
  });

  rows.innerHTML = visible.map(campaign => `<tr><td><span class="campaign-thumb ${campaign.status === 'active' ? 'thumb-one' : campaign.status === 'review' ? 'thumb-three' : 'thumb-two'}">${campaign.name[0]}</span><span><strong>${campaign.name}</strong><small>${campaign.client} · ${campaign.code}</small></span></td><td>${campaign.period}</td><td><span class="status ${campaign.status}">● ${campaign.statusLabel}</span></td><td><span class="cell-state ${campaign.payment}">${campaign.paymentLabel}</span></td><td><span class="cell-state ${campaign.video}">${campaign.videoLabel}</span></td><td>${campaign.insertions}</td><td><strong>${campaign.value}</strong></td><td><button class="row-action" data-code="${campaign.code}">Ver</button></td></tr>`).join('');
  emptyState.hidden = visible.length > 0;
  document.querySelector('#total-count').textContent = String(visible.length).padStart(2, '0');
  document.querySelector('#review-count').textContent = String(visible.filter(c => c.status === 'review').length).padStart(2, '0');
  document.querySelector('#paid-count').textContent = String(visible.filter(c => c.payment === 'paid').length).padStart(2, '0');
  document.querySelector('#active-count').textContent = String(visible.filter(c => c.status === 'active').length).padStart(2, '0');
  document.querySelector('#result-label').textContent = `${String(visible.length).padStart(2, '0')} registros encontrados`;
};

[searchInput, statusFilter, periodFilter].forEach(control => control.addEventListener('input', render));
document.querySelector('#export-button').addEventListener('click', () => alert('A exportação será habilitada junto à API administrativa.'));
rows.addEventListener('click', event => {
  const button = event.target.closest('.row-action');
  if (button) alert(`Detalhes da campanha ${button.dataset.code} serão conectados ao painel operacional.`);
});
render();
