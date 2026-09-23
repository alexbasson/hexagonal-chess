require 'user_management/user_id'
require 'user_management/user'
require 'user_management/user_repository'
require 'user_management/list_users'

RSpec.describe UserManagement::ListUsers do
  subject(:list_users) { described_class.new(user_repository) }

  let(:user_repository) { instance_double(UserManagement::UserRepository) }
  let(:user)            { UserManagement::User.new(id: UserManagement::UserId.new(value: 'u1'), email: 'a@b.com', display_name: 'Alice') }

  it 'returns all users' do
    allow(user_repository).to receive(:find_all).and_return([user])

    expect(list_users.call).to eq([user])
  end

  it 'returns empty when no users' do
    allow(user_repository).to receive(:find_all).and_return([])

    expect(list_users.call).to eq([])
  end
end
