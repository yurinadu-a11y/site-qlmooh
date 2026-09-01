const DAILY_INSERTIONS = 4320;
const MIN_DAYS = 1;
const MAX_DAYS = 365;
const PRICE_MAX = 99;
const PRICE_MIN = 59;

const money = value => value.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
const dailyPrice = days => {
  const ratio = (days - MIN_DAYS) / (MAX_DAYS - MIN_DAYS);
  return Math.round((PRICE_MAX - (PRICE_MAX - PRICE_MIN) * ratio) * 100) / 100;
};

const daysInput = document.querySelector('#days');
const daysOutput = document.querySelector('#days-output');
const totalOutput = document.querySelector('#total-output');
const insertionsOutput = document.querySelector('#insertions-output');

function updateSimulation() {
  const days = Number(daysInput.value);
  const total = Math.round(dailyPrice(days) * days * 100) / 100;
  daysOutput.value = `${days} ${days === 1 ? 'dia' : 'dias'}`;
  daysOutput.textContent = daysOutput.value;
  totalOutput.textContent = money(total);
  insertionsOutput.textContent = `${(DAILY_INSERTIONS * days).toLocaleString('pt-BR')} inserções previstas`;
}

daysInput.addEventListener('input', updateSimulation);
document.querySelectorAll('[data-scroll]').forEach(button => button.addEventListener('click', () => document.querySelector(button.dataset.scroll)?.scrollIntoView({ behavior: 'smooth' })));
updateSimulation();
