require 'user_management/user_id'
require 'user_management/friendship'
require 'user_management/friendship_repository'
require 'user_management/in_memory_friendship_repository'

RSpec.describe UserManagement::InMemoryFriendshipRepository do
  subject(:repo) { described_class.new }

  let(:owner_id)    { UserManagement::UserId.new(value: 'u1') }
  let(:friend_id)   { UserManagement::UserId.new(value: 'u2') }
  let(:friendship)  { UserManagement::Friendship.new(owner_id: owner_id, friend_id: friend_id) }

  it 'returns empty when no friendships' do
    expect(repo.find_by_owner_id(owner_id)).to eq([])
  end

  it 'saves and finds by owner id' do
    repo.save(friendship)
    expect(repo.find_by_owner_id(owner_id)).to eq([friendship])
  end

  it 'only returns friendships for the specified owner' do
    other = UserManagement::Friendship.new(owner_id: UserManagement::UserId.new(value: 'u3'), friend_id: friend_id)
    repo.save(friendship)
    repo.save(other)
    expect(repo.find_by_owner_id(owner_id)).to eq([friendship])
  end

  it 'deletes a friendship' do
    repo.save(friendship)
    repo.delete_by_owner_and_friend(owner_id, friend_id)
    expect(repo.find_by_owner_id(owner_id)).to eq([])
  end

  it 'does nothing when deleting a non-existent friendship' do
    expect { repo.delete_by_owner_and_friend(owner_id, friend_id) }.not_to raise_error
  end
end
