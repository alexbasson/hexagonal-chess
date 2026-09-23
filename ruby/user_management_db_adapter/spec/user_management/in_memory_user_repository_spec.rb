require 'user_management/user_id'
require 'user_management/user'
require 'user_management/user_repository'
require 'user_management/in_memory_user_repository'

RSpec.describe UserManagement::InMemoryUserRepository do
  subject(:repo) { described_class.new }

  let(:user_id) { UserManagement::UserId.new(value: 'u1') }
  let(:user)    { UserManagement::User.new(id: user_id, email: 'alice@example.com', display_name: 'Alice') }

  it 'returns nil when not found' do
    expect(repo.find_by_id(user_id)).to be_nil
  end

  it 'saves and finds by id' do
    repo.save(user)
    expect(repo.find_by_id(user_id)).to eq(user)
  end

  it 'returns all saved users' do
    user2 = UserManagement::User.new(id: UserManagement::UserId.new(value: 'u2'), email: 'bob@example.com', display_name: 'Bob')
    repo.save(user)
    repo.save(user2)
    expect(repo.find_all).to eq([user, user2])
  end

  it 'deletes a user' do
    repo.save(user)
    repo.delete(user_id)
    expect(repo.find_by_id(user_id)).to be_nil
  end

  it 'overwrites on save' do
    repo.save(user)
    updated = UserManagement::User.new(id: user_id, email: 'new@example.com', display_name: 'Alice')
    repo.save(updated)
    expect(repo.find_by_id(user_id)).to eq(updated)
  end
end
