const api = async (path) => {
  const response = await fetch(path);
  if (!response.ok) throw new Error(`Request failed: ${response.status}`);
  return response.json();
};

const formatQar = value => new Intl.NumberFormat('en-QA', { style: 'currency', currency: 'QAR' }).format(Number(value));
const label = key => key.replace(/([A-Z])/g, ' $1').replace(/^./, c => c.toUpperCase());

async function loadDashboard() {
  try {
    const [summary, patients, appointments, inventory, doctors] = await Promise.all([
      api('/api/dashboard'), api('/api/patients'), api('/api/appointments'), api('/api/inventory'), api('/api/doctors')
    ]);

    document.querySelector('#stats').innerHTML = Object.entries(summary).slice(0, 8).map(([key, value]) => `
      <article class="stat"><strong>${key === 'outstandingBalance' ? formatQar(value) : value}</strong><span>${label(key)}</span></article>
    `).join('');

    const patientById = Object.fromEntries(patients.map(patient => [patient.id, patient]));
    const doctorById = Object.fromEntries(doctors.map(doctor => [doctor.id, doctor]));

    document.querySelector('#appointments').innerHTML = appointments.map(item => `
      <tr><td>${new Date(item.startTime).toLocaleString()}</td><td>${patientById[item.patientId]?.name ?? item.patientId}</td>
      <td>${doctorById[item.doctorId]?.name ?? item.doctorId}</td><td>${item.reason}</td><td><span class="badge">${item.status}</span></td></tr>
    `).join('') || '<tr><td colspan="5">No appointments.</td></tr>';

    document.querySelector('#patients').innerHTML = patients.map(patient => `
      <div class="list-item"><div><strong>${patient.name}</strong><div class="muted">${patient.mrn} · ${patient.gender} · ${patient.age} years</div></div><span>${patient.bloodType}</span></div>
    `).join('');

    document.querySelector('#inventory').innerHTML = inventory.map(item => `
      <div class="list-item"><div><strong>${item.name}</strong><div class="muted">${item.category} · reorder at ${item.reorderLevel}</div></div><span class="${item.lowStock ? 'low' : ''}">${item.quantity}${item.lowStock ? ' LOW' : ''}</span></div>
    `).join('');
  } catch (error) {
    document.querySelector('main').insertAdjacentHTML('afterbegin', `<p class="error">Could not load dashboard: ${error.message}</p>`);
  }
}

document.querySelector('#refresh').addEventListener('click', loadDashboard);
loadDashboard();
